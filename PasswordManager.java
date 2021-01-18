import java.io.File; //Import File Class
import java.io.FileWriter; //Import FileWriter class
import java.io.IOException; //Import the IOException class to handle errors
import java.io.FileNotFoundException; //Import this class to handle errors
import java.util.Scanner; //Import Scanner class to read text files
import java.security.SecureRandom; // Import SecureRandom to ensure cryptographically strong password
import java.io.UnsupportedEncodingException; //Import class for non-supported charcater encoding
import java.security.MessageDigest; //Import class to take in data and output fixed-length hash value
import java.security.NoSuchAlgorithmException; //Import class for cryptographic algorithm equested but not available in the environment
import java.util.Arrays; //Import class to manipulate arrays
import java.util.Base64; //Import class for static methods for obtaining encoders and decoders for Base64 encoding scheme
import javax.crypto.Cipher; //Import class to provide functionality of a cryptographic cipher for encyption and decryption
import javax.crypto.spec.SecretKeySpec; //Import class to construct a SecretKey from byte array

public class PasswordManager {
    // Create file to store usernames and passwords
    public static boolean createFile() {
        boolean exist = false;
        try {
            File myObj = new File("database.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                exist = true;
                System.out.println("Database exists. Please login.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return exist;
    }

    // Encrypt password
    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey(String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String strToEncrypt, String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    // Output decrypted password
    public static String decrypt(String strToDecrypt, String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

    // 0. First time setup
    private static void createProf() {
        System.out.println("Please enter username: ");
        String user_in = System.console().readLine();
        System.out.println("Please enter password: ");
        String pass_in = System.console().readLine();
        System.out.println("Please re-enter password: ");
        String pass_in2 = System.console().readLine();
        boolean same = false;
        final String secretKey = "PasswordManager";
        while (!same) {
            if (pass_in.equals(pass_in2)) {
                try {
                    FileWriter myWriter = new FileWriter("database.txt", true);
                    myWriter.write("Master Username: " + user_in + System.getProperty("line.separator"));
                    myWriter.write(
                            "Master Password: " + encrypt(pass_in, secretKey) + System.getProperty("line.separator"));
                    myWriter.write("" + System.getProperty("line.separator"));
                    myWriter.close();
                    System.out.println("Successfully created profile.");
                    System.out.println("");
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
                same = true;
            } else {
                System.out.println("Passwords do not match.");
                System.out.println("Please enter password:");
                pass_in = System.console().readLine();
                System.out.println("Please re-enter password: ");
                pass_in2 = System.console().readLine();
            }
        }
    }

    // 1. Check username and password
    private static boolean checkProf() {
        boolean access = false;
        final String secretKey = "PasswordManager";
        try {
            File myObj = new File("database.txt");
            Scanner myReader = new Scanner(myObj);
            String user_check = myReader.nextLine();
            String pass_check = myReader.nextLine();
            user_check = user_check.substring(user_check.lastIndexOf(" ") + 1);
            pass_check = decrypt(pass_check.substring(pass_check.lastIndexOf(" ") + 1), secretKey);
            // System.out.println(user_check);
            // System.out.println(pass_check);
            System.out.println("Username: ");
            String user_inp = System.console().readLine();
            System.out.println("Password: ");
            String pass_inp = System.console().readLine();
            if (user_check.equals(user_inp) & pass_check.equals(pass_inp)) {
                access = true;
                System.out.println("Access Granted.");
            } else {
                System.out.println("Incorrect username or password.");
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return access;
    }

    // 2. Store usernames and passwords
    private static void storeInfo() {
        System.out.println("Please enter site name:");
        String site = System.console().readLine();
        System.out.println("Please enter site username:");
        String user = System.console().readLine();
        System.out.println("Please enter site password:");
        String pass = System.console().readLine();
        final String secretKey = "PasswordManager";
        try {
            FileWriter myWriter = new FileWriter("database.txt", true);
            myWriter.write(site + ":" + System.getProperty("line.separator"));
            myWriter.write("username: " + user + System.getProperty("line.separator"));
            myWriter.write("password: " + encrypt(pass, secretKey) + System.getProperty("line.separator"));
            myWriter.write("" + System.getProperty("line.separator"));
            myWriter.close();
            System.out.println("Successfully entered login for " + site);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    // 3. Output desired username and password
    private static void outInfo(String site) {
        String user = "user";
        String pass = "pass";
        final String secretKey = "PasswordManager";
        try {
            File myObj = new File("database.txt");
            Scanner myReader = new Scanner(myObj);
            boolean found = false;
            while (!found) {
                if (myReader.nextLine().contains(site)) {
                    user = myReader.nextLine();
                    System.out.println(user);
                    pass = myReader.nextLine();
                    System.out.println("password: " + decrypt(pass.substring(pass.lastIndexOf(" ") + 1), secretKey));
                    found = true;
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    // 4. Edit stored username and password
    private static void editInfo(String site) {
        String user = "user";
        String pass = "pass";
        String site1 = "site1";
        String user1 = "user1";
        String pass1 = "pass1";
        final String secretKey = "PasswordManager";
        try {
            File myObj = new File("database.txt");
            Scanner myReader = new Scanner(myObj);
            boolean found = false;
            while (!found) {
                if (myReader.nextLine().contains(site)) {
                    user = myReader.nextLine();
                    pass = myReader.nextLine();
                    found = true;
                }
            }
            myReader.close();
            Scanner myReader2 = new Scanner(myObj);
            StringBuffer buffer = new StringBuffer();
            while (myReader2.hasNextLine()) {
                buffer.append(myReader2.nextLine() + System.getProperty("line.separator"));
            }
            String fileContents = buffer.toString();
            myReader2.close();
            System.out.println("What would you like to change? ");
            String change = System.console().readLine();
            change = change.toLowerCase();
            if (change.contains("user")) {
                System.out.println("What is your new username? ");
                user1 = System.console().readLine();
                fileContents = fileContents.replace(user, ("username: " + user1));
            } else if (change.contains("pass")) {
                System.out.println("What is your new password? ");
                pass1 = System.console().readLine();
                fileContents = fileContents.replace(pass, ("password: " + encrypt(pass1, secretKey)));
            } else if (change.contains("both")) {
                System.out.println("What is your new username? ");
                user1 = System.console().readLine();
                fileContents = fileContents.replace(user, ("username: " + user1));
                System.out.println("What is your new password? ");
                pass1 = System.console().readLine();
                fileContents = fileContents.replace(pass, ("password: " + encrypt(pass1, secretKey)));
            } else if (change.contains("site")) {
                System.out.println("What is the new site? ");
                site1 = System.console().readLine();
                fileContents = fileContents.replace(site, (site1 + ":"));
            }
            FileWriter myWriter = new FileWriter("database.txt");
            myWriter.write(fileContents);
            myWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    // 5. Generate random password
    private static String randPassword(int len) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < len; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        boolean exist = createFile();
        boolean access = false;
        if (exist) {
            access = checkProf();
            while (!access) {
                access = checkProf();
            }
        } else {
            createProf();
            access = true;
        }
        while (access) {
            System.out.println("What would you like to do? ");
            String key = System.console().readLine();
            if (key.equalsIgnoreCase("add")) {
                storeInfo();
                System.out.println("Are you done? ");
                String done = System.console().readLine();
                if (done.equalsIgnoreCase("no")) {
                } else {
                    access = false;
                }
            } else if (key.equalsIgnoreCase("see")) {
                System.out.println("Please enter the site for which you wish to check the username and password:");
                String site = System.console().readLine();
                outInfo(site);
                System.out.println("Are you done? ");
                String done = System.console().readLine();
                if (done.equalsIgnoreCase("no")) {
                } else {
                    access = false;
                }
            } else if (key.equalsIgnoreCase("generate")) {
                System.out.println("Please specify length of password: ");
                int len = Integer.parseInt(System.console().readLine());
                System.out.println(randPassword(len));
                System.out.println("Are you done? ");
                String done = System.console().readLine();
                if (done.equalsIgnoreCase("no")) {
                } else {
                    access = false;
                }
            } else if (key.equalsIgnoreCase("edit")) {
                System.out.println("Please enter the site for which you wish to change the username or password:");
                String site = System.console().readLine();
                editInfo(site);
                System.out.println("Are you done? ");
                String done = System.console().readLine();
                if (done.equalsIgnoreCase("no")) {
                } else {
                    access = false;
                }
            } else {
                access = false;
            }
        }
        if (access == false) {
            System.out.println("Have a nice day!");
        }
    }
}
