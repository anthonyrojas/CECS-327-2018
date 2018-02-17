import java.util.*;
import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.apache.commons.net.*;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.io.*;
import org.apache.commons.net.util.*;
public class Assn3 {
    public static String FTP_PROMPT = "ftp> ";
    public static void main(String[] args){
        if(args.length <= 0){
            System.out.println("You must enter the IP address and username:password " +
                    "\nRun the program again");
            System.exit(1);
        }
        String IP=args[0];
        String idPwd = args[1];
        if(!idPwd.contains(":")){
            System.out.println("The user and password must be input using the following format... " +
                    "\n user:password" +
                    "\nRun the program again using this format.");
            System.exit(1);
        }
        String[] userInfo = idPwd.split(":");
        String user = userInfo[0];
        String password = userInfo[1];
        connectToServer(IP, user, password);
    }

    public static void connectToServer(String IP, String user, String password){
        FTPClient ftp = new FTPClient();
        try {
            System.out.println("Connecting to the server as " + user + ":" + password);
            ftp.connect(IP);
            ftp.login(user, password);
            System.out.println(ftp.getReplyString());
            displayMenu();
            inputMenuOption(ftp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void inputMenuOption(FTPClient ftp){
        Scanner ftpCommandIn = new Scanner(System.in);
        while(true){
            String ftpInput;
            System.out.print(FTP_PROMPT);
            ftpInput = ftpCommandIn.nextLine();
            String[] ftpInputs = ftpInput.split("\\s");
            String ftpCommand = ftpInputs[0];
            switch (ftpCommand){
                case "ls": displayLocalFiles(ftp, ftpInputs);
                    break;
                case "mkdir":
                    makeDirectory(ftp, ftpInputs);
                    break;
                case "cd":
                    changeDirectory(ftp, ftpInputs);
                    break;
                case "delete":
                    deleteFile(ftp, ftpInputs);
                    break;
                case "get":
                    getFile(ftp, ftpInputs);
                    break;
                case "quit":
                    System.out.println("goodbye");
                    System.exit(0);
                    break;
                case "--help":
                    displayMenu();
                    break;
                default: System.out.println("Invalid option." +
                        " If you need to know what options are available, then type --help");
                    break;
            }
        }
    }

    public static void displayLocalFiles(FTPClient ftp, String[] ftpInputs){
        try {
            FTPFile[] ftpFiles = ftp.listFiles();
            if(ftpFiles == null || ftpFiles.length <= 0){
                System.out.println("current directory: " + ftp.printWorkingDirectory());
                System.out.println("No files in the current directory.");
            }else{
                System.out.println("current directory: " + ftp.printWorkingDirectory());
                System.out.format(" %-50s%-20s\n", "NAME", "TYPE");
                for (FTPFile f : ftpFiles) {
                    if(f.isDirectory()){
                        System.out.format(" %-50s%-20s\n", f.getName(), "directory");
                    }else if(f.isFile()){
                        System.out.format(" %-50s%-20s\n", f.getName(), "file");
                    }else if(f.isSymbolicLink()){
                        System.out.format(" %-50s%-20s\n", f.getName(), "symbolic link");
                    }else{
                        System.out.format(" %-50s%-20s\n", f.getName(), "unknown");
                    }
                    //System.out.println(f.getName() + "\t\t\t" + f.getType());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void makeDirectory(FTPClient ftp, String[] ftpInputs){
        if(ftpInputs.length < 2){
            System.out.println("Directory name not specified.");
            return;
        }else{
            String dirName = ftpInputs[1];
            try{
                ftp.makeDirectory(dirName);
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static void changeDirectory(FTPClient ftp, String[] ftpInputs){
        if(ftpInputs.length < 2){
            System.out.println("Directory name not specified.");
            return;
        }else{
            String dirName = ftpInputs[1];
            try {
                if(dirName.equalsIgnoreCase("..")){
                    ftp.changeToParentDirectory();
                    System.out.println("current directory: " + ftp.printWorkingDirectory());
                }else{
                    ftp.changeWorkingDirectory(dirName);
                    System.out.println("current directory: " + ftp.printWorkingDirectory());
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void deleteFile(FTPClient ftp, String[] ftpInputs){
        if(ftpInputs.length < 2){
            System.out.println("File name not specified.");
            return;
        }else{
            String fileName = ftpInputs[1];
            try{
                ftp.deleteFile(fileName);
                System.out.println("File deleted: " + fileName);
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static void checkGetType(FTPClient ftp, String[] ftpInputs){
        //check if the file i am getting is a directory or file
    }

    public static void getFile(FTPClient ftp, String[] ftpInputs){
        if(ftpInputs.length < 2){
            System.out.println("File name not specified");
            return;
        }else{
            try{
                File f = new File(ftpInputs[1]);
                FileOutputStream fop = new FileOutputStream(f);
                ftp.retrieveFile(ftpInputs[1], fop);
                fop.close();
            }catch(IOException e ){
                System.out.println(e.getMessage());
            }
        }
    }

    public static void displayMenu(){
        String options = " ls" +
                "\n cd <directory name>" +
                "\n cd .." +
                "\n delete <file name>" +
                "\n get <file name>" +
                "\n get <directory name>" +
                "\n put <file name>" +
                "\n mkdir <directory name>" +
                "\n rmdir <directory name>" +
                "\n --help : will display this menu" +
                "\n quit : exit the program";
        System.out.println("Available functions are:\n" + options);
    }
}
