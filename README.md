# TLS Mutual Authentication — Kotlin Reference Implementation

A production-grade demonstration of TLS 1.3 server–client communication using asymmetric cryptography, X.509 certificate chains, and the Java Secure Socket Extension (JSSE) stack, implemented in Kotlin 2.x.

---

## Table of Contents

1. [Cryptographic Foundations](#1-cryptographic-foundations)
2. [Certificate Hierarchy](#2-certificate-hierarchy)
3. [TLS Handshake — Full Protocol Flow](#3-tls-handshake--full-protocol-flow)
4. [KeyStore vs TrustStore](#4-keystore-vs-truststore)
5. [JSSE Architecture in this Codebase](#5-jsse-architecture-in-this-codebase)
6. [Certificate Provisioning Pipeline](#6-certificate-provisioning-pipeline)
7. [Threat Model & Security Properties](#7-threat-model--security-properties)
8. [Project Structure](#8-project-structure)
9. [Running Locally](#9-running-locally)

---

## 1. Cryptographic Foundations

TLS security rests on **asymmetric (public-key) cryptography**. A key pair has one irreducible property:

> Data encrypted with the **public key** can _only_ be decrypted with the corresponding **private key**, and vice versa.

This asymmetry enables two independent security goals over the same key pair:

| Goal | Operation | Who holds the key |
|---|---|---|
| **Confidentiality** | Encrypt with server's public key | Client encrypts → Server decrypts |
| **Authentication** | Sign with private key, verify with public key | Server signs → Client verifies |

In TLS 1.3 the asymmetric keys are used exclusively for **authentication and key exchange** (via ECDHE). Bulk data is always encrypted with a symmetric session key derived during the handshake, keeping latency minimal.

---

## 2. Certificate Hierarchy

This repo uses a three-tier PKI matching production CA structures:

```mermaid
graph TD
    RootCA["🔐 Root CA<br/><i>root-ca.conf</i><br/>Self-signed · Offline · Trust anchor"]
    ServerCert["📄 Server Certificate<br/><i>restapi-server.cert</i><br/>CN=restapi · Signed by Root CA"]
    ClientCert["📄 Client Certificate<br/><i>restapi-client.cert</i><br/>CN=restapi-client · Signed by Root CA"]
    ServerKS["🗄️ Server KeyStore<br/><i>PKCS#12</i><br/>Private key + Server cert chain"]
    ClientTS["🗄️ Client TrustStore<br/><i>DER / PKCS#12</i><br/>Root CA cert only"]

    RootCA -->|signs| ServerCert
    RootCA -->|signs| ClientCert
    ServerCert -->|loaded into| ServerKS
    RootCA -->|exported to| ClientTS
```

The Root CA **never** appears on the wire. It is the offline trust anchor whose public key, embedded in the client's TrustStore, allows the client to verify the server's certificate without any prior connection.

---

## 3. TLS Handshake — Full Protocol Flow

TLS 1.3 collapses the classic 2-RTT handshake of TLS 1.2 into **1-RTT** (0-RTT for session resumption).

```mermaid
sequenceDiagram
    autonumber
    participant C as Client<br/>(HttpTlsClient)
    participant S as Server<br/>(HttpTlsServer)

    Note over C,S: TCP connection established

    C->>S: ClientHello<br/>supported cipher suites, key_share (ECDHE pubkey), TLS version

    S->>C: ServerHello<br/>chosen cipher suite, key_share (ECDHE pubkey)
    S->>C: {Certificate}<br/>server X.509 cert chain (encrypted)
    S->>C: {CertificateVerify}<br/>signature over handshake transcript using server private key
    S->>C: {Finished}<br/>HMAC over transcript with server handshake key

    Note over C: Verify Certificate chain → Root CA in TrustStore<br/>Verify CertificateVerify signature<br/>Derive session keys from ECDHE shared secret

    C->>S: {Finished}<br/>HMAC over transcript with client handshake key

    Note over C,S: ✅ Handshake complete — symmetric session keys established

    C->>S: {Application Data}<br/>HTTP request (AES-GCM encrypted)
    S->>C: {Application Data}<br/>HTTP/1.1 200 (AES-GCM encrypted)
```

**Key derivation (HKDF chain):**

$$
\text{SharedSecret} \xrightarrow{\text{HKDF-Extract}} \text{HandshakeSecret} \xrightarrow{\text{HKDF-Expand}} \begin{cases} \text{client\_handshake\_key} \\ \text{server\_handshake\_key} \\ \text{client\_application\_key} \\ \text{server\_application\_key} \end{cases}
$$

Each direction uses an independent key — compromise of one does not expose the other (forward secrecy via ephemeral ECDHE).

---

## 4. KeyStore vs TrustStore

Both are `java.security.KeyStore` instances at the JVM level. The distinction is **semantic and role-based**:

```mermaid
flowchart LR
    subgraph Server Process
        direction TB
        KS["KeyStore<br/>─────────────────<br/>• Server private key (RSA/EC)<br/>• Server certificate<br/>• Root CA certificate<br/>─────────────────<br/>javax.net.ssl.keyStore<br/>KeyManagerFactory (SunX509)"]
    end

    subgraph Client Process
        direction TB
        TS["TrustStore<br/>─────────────────<br/>• Root CA certificate only<br/>• NO private keys<br/>─────────────────<br/>javax.net.ssl.trustStore<br/>TrustManagerFactory (SunX509)"]
    end

    subgraph Wire
        direction LR
        H(["TLS Handshake"])
    end

    KS -- "Server presents cert<br/>signed by Root CA" --> H
    H -- "Client verifies cert<br/>against trusted Root CA" --> TS
```

| Property | KeyStore | TrustStore |
|---|---|---|
| Contains private keys | ✅ Yes | ❌ Never |
| Contains certificates | ✅ Own cert + chain | ✅ Trusted CA certs |
| Protects | Server identity / decryption | Client's list of trusted issuers |
| Exposed on wire | Certificate (public) only | Never sent |
| JVM property | `javax.net.ssl.keyStore` | `javax.net.ssl.trustStore` |
| Factory | `KeyManagerFactory` | `TrustManagerFactory` |
| Format in this repo | PKCS#12 (`.p12`) | DER (`.der`) / PKCS#12 |

---

## 5. JSSE Architecture in this Codebase

```mermaid
flowchart TD
    subgraph server["server module"]
        CS["CertificateStore.createTLSContext()"]
        KSL["KeyStore.load(keyStoreFile, password)"]
        KMF["KeyManagerFactory.getInstance('SunX509')\n.init(keyStore, password)"]
        TMF_S["TrustManagerFactory.getInstance('SunX509')\n.init(keyStore)"]
        SCTX["SSLContext.getInstance(tlsVersion)\n.init(km, tm, null)"]
        SSF["SSLContext.serverSocketFactory"]
        SSS["SSLServerSocket.accept() → SSLSocket"]
        SH["SSLSocket.startHandshake()"]
        IO_S["InputStream / OutputStream\n(application data, post-handshake)"]

        CS --> KSL --> KMF & TMF_S --> SCTX --> SSF --> SSS --> SH --> IO_S
    end

    subgraph client["client module"]
        CT["ClientTruststore.createTLSContext()"]
        KSL_C["KeyStore.load(trustStoreFile, password)"]
        KMF_C["KeyManagerFactory.getInstance('SunX509')"]
        TMF_C["TrustManagerFactory.getInstance('SunX509')"]
        CCTX["SSLContext.getInstance(tlsVersion)\n.init(km, tm, null)"]
        CSF["SSLContext.socketFactory"]
        CSS["SSLSocket.connect(host, port)"]
        SH_C["SSLSocket.startHandshake()"]
        IO_C["PrintWriter / BufferedReader\n(HTTP over TLS)"]

        CT --> KSL_C --> KMF_C & TMF_C --> CCTX --> CSF --> CSS --> SH_C --> IO_C
    end

    SH <-->|"TLS Record Layer"| SH_C
```

`SSLContext` is the root factory. It is **not thread-safe to initialize** but its derived socket factories are, making the pattern of creating one `SSLContext` per server startup and reusing the `ServerSocketFactory` correct for concurrent connections.

---

## 6. Certificate Provisioning Pipeline

Certificates are generated via shell scripts in `server/conf3/`. The chain mirrors a real CA workflow:

```mermaid
flowchart LR
    A(["create-root-cert.sh"])
    B(["create-server-cert.sh"])
    C(["create-client-cert.sh"])

    A -->|"generates"| RootKey["root-ca.key\n(RSA private key)"]
    A -->|"generates"| RootCert["root-ca.crt\n(self-signed X.509)"]

    B -->|"generates"| SrvKey["server.key"]
    B -->|"generates"| SrvCSR["server.csr\n(PKCS#10)"]
    RootKey & RootCert -->|"sign CSR"| B
    B -->|"generates"| SrvCert["restapi-server.cert\n(X.509 signed by Root CA)"]
    SrvKey & SrvCert -->|"bundle"| SrvP12["server.p12\n(PKCS#12 KeyStore)"]

    C -->|"generates"| CliKey["client.key"]
    C -->|"generates"| CliCSR["client.csr\n(PKCS#10)"]
    RootKey & RootCert -->|"sign CSR"| C
    C -->|"generates"| CliCert["restapi-client.cert"]
    RootCert -->|"export DER"| ClientDER["restapi.der\n(Client TrustStore)"]
```

**PKCS#10 (CSR)** is the standard wire format for certificate signing requests — it contains the subject's public key and Distinguished Name, signed by the subject's private key to prove key possession, but carries no trust itself until a CA signs it into an X.509 certificate.

---

## 7. Threat Model & Security Properties

| Property | Mechanism | Status in this repo |
|---|---|---|
| **Confidentiality** | AES-GCM session key derived via ECDHE | ✅ Enforced by TLS |
| **Server Authentication** | Client verifies server cert chain to Root CA in TrustStore | ✅ `TrustManagerFactory` |
| **Client Authentication (mTLS)** | Server verifies client cert — requires `SSLServerSocket.needClientAuth = true` | ⚠️ Not enabled — server accepts any client |
| **Forward Secrecy** | Ephemeral ECDHE key exchange — session keys not derivable from long-term keys | ✅ TLS 1.3 mandatory |
| **Replay Protection** | Sequence numbers in TLS Record Layer + session tickets with age limit | ✅ Enforced by TLS |
| **Cipher Suite Negotiation** | `SSLSocket.enabledCipherSuites = supportedCipherSuites` | ⚠️ Enables ALL suites — pin to strong suites in production |
| **Certificate Revocation** | OCSP / CRL | ❌ Not implemented |
| **Private Key Protection** | PKCS#12 password-encrypted at rest | ✅ Password required to load |

**To enable mTLS**, add to `HttpTlsServer`:
```kotlin
(tlSecuredServerSocket as SSLServerSocket).needClientAuth = true
```
The server's `TrustManagerFactory` must then be initialized with a store containing the Root CA that signed the client certificate.

---

## 8. Project Structure

```
tls.kotlin/
├── server/
│   ├── build.gradle                          # Kotlin 2.1.21, application plugin
│   ├── conf3/
│   │   ├── create-root-cert.sh               # Step 1: generate Root CA
│   │   ├── create-server-cert.sh             # Step 2: server cert signed by Root CA
│   │   ├── create-client-cert.sh             # Step 3: client cert signed by Root CA
│   │   ├── restapi-server.cert               # X.509 PEM server certificate
│   │   └── restapi-client.cert               # X.509 PEM client certificate
│   └── src/main/kotlin/server/api/
│       ├── Server.kt                         # Entry point — wires port/keystore/password
│       ├── HttpTlsServer.kt                  # SSLServerSocket accept loop + NonBlockingSecuredConnectionHandler
│       └── tls/
│           └── CertificateStore.kt           # KeyStore → KeyManagerFactory → SSLContext factory
├── client/
│   ├── build.gradle                          # Kotlin 2.1.21, application plugin
│   ├── conf/
│   │   └── restapi.der                       # Root CA cert (DER) — client TrustStore
│   └── src/main/kotlin/client/api/
│       ├── Client.kt                         # Entry point — wires host/port/truststore
│       ├── HttTlsClient.kt                   # SSLSocket connect + ClientConnectionThread
│       └── tls/
│           └── ClientTruststore.kt           # KeyStore → TrustManagerFactory → SSLContext factory
```

---

## 9. Running Locally

**Prerequisites:** JDK 21+, Gradle 8+

```bash
# 1. Generate PKI artifacts (one-time)
cd server/conf3
./create-root-cert.sh
./create-server-cert.sh
./create-client-cert.sh

# 2. Start the server (blocks, listening on configured port)
cd server
gradle run

# 3. In a separate terminal, run the client
cd client
gradle run
```

**Expected handshake output (server):**
```
[INFO] HttpTlsServer TLSv1 server started!!!
[INFO] NonBlockingSecuredConnectionHandler TLSSession :
    Protocol : TLSv1.3
    Cipher suite : TLS_AES_256_GCM_SHA384
```

**Expected output (client):**
```
[INFO] HttpTlsClient TLSv1 client started
[INFO] ClientConnection TLSSession :
    TLS Protocol : TLSv1.3
    TLS Cipher suite : TLS_AES_256_GCM_SHA384
[INFO] ClientConnection received : HTTP/1.1 200
```

---

## References

- [RFC 8446 — TLS 1.3](https://datatracker.ietf.org/doc/html/rfc8446)
- [JSSE Reference Guide (OpenJDK 21)](https://docs.oracle.com/en/java/javase/21/security/java-secure-socket-extension-jsse-reference-guide.html)
- [X.509 Certificate Format (RFC 5280)](https://datatracker.ietf.org/doc/html/rfc5280)
- [PKCS#12 (RFC 7292)](https://datatracker.ietf.org/doc/html/rfc7292)
- [Elliptic Curve Diffie-Hellman Ephemeral (ECDHE)](https://en.wikipedia.org/wiki/Elliptic-curve_Diffie%E2%80%93Hellman)
- [HKDF — RFC 5869](https://datatracker.ietf.org/doc/html/rfc5869)

