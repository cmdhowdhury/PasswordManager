# PasswordManager

### Description
Through this lab, we take a look into the mechanism of password managers, compare popular password managers, the pros and cons of password managers, and a password manager that I built with some external assistance.

### Prerequisites
1. Terminal 
2. Java installed
3. Clone git repo

### Launch Instructions
```
$ git clone https://github.com/cmdhowdhury/PasswordManager.git

$ cd PasswordManager

$ make
```

### Functions
Running the java program will create a txt file with the stored information under the name database.txt

If this is your first time opening the program, the program will ask you to create a profile by setting a username and password. This information will allow you to access the rest of the program. 

After you set up a profile, you have three options:
```
add: will request a site, the username for that site and the password for that site and store that information in database.txt
```
```
see: will output the username and password of a desired site, when asked for desired site, entry is white-space sensitive
```
```
generate: will output a random password of desired length, includes numbers and letters, not symbols
```
If you open database.txt, you will only see the username and site in plaintext, the passwords will be encrypted through AES encryption.
### Credits/Citation
1. [Information regarding working with files](https://www.w3schools.com/java/java_files.asp)

2. [Code for generating a random password](https://www.techiedelight.com/generate-random-alphanumeric-password-java/)

3. [Code for Encrypting and Decrypting passwords](https://howtodoinjava.com/java/java-security/java-aes-encryption-example/)
