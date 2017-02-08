create keystore with public/private keys
------------------------------------------

```bash
prayagupd at prayagupd-vbox in /media/sf_programming/https-server-kotlin/src
$ mkdir conf
```

```
$ keytool -genkey -alias eccount -keyalg RSA -keystore conf/eccountKeyStore.jks
Enter keystore password:  
Re-enter new password: 
What is your first and last name?
  [Unknown]:  Prayag
What is the name of your organizational unit?
  [Unknown]:  dvnhlsm
What is the name of your organization?
  [Unknown]:  dvnhlsm
What is the name of your City or Locality?
  [Unknown]:  SEA
What is the name of your State or Province?
  [Unknown]:  WA
What is the two-letter country code for this unit?
  [Unknown]:  US
Is CN=Prayag, OU=dvnhlsm, O=dvnhlsm, L=SEA, ST=WA, C=US correct?
  [no]:  Yes

Enter key password for <eccount>
	(RETURN if same as keystore password): 
```