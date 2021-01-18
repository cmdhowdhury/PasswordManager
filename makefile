run:PasswordManager.class
	java PasswordManager

PasswordManager.class: PasswordManager.java
	javac PasswordManager.java

clean:
	rm *.class
