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
            String ftpCommand="";
            String names="";
            if(!ftpInput.contains(" ")){
                ftpCommand = ftpInput;
            }else{
                ftpCommand = ftpInput.substring(0, ftpInput.indexOf(" "));
                names = ftpInput.substring(ftpInput.indexOf(" ")+1);
            }
            /*String[] ftpInputs = ftpInput.split("\\s");
            String ftpCommand = ftpInputs[0];*/
            switch (ftpCommand){
                case "ls": displayLocalFiles(ftp);
                    break;
                case "mkdir":
                    makeDirectory(ftp, names);
                    break;
                case "cd":
                    changeDirectory(ftp, names);
                    break;
                case "delete":
                    deleteFile(ftp, names);
                    break;
                case "get":
                    getDirectory(ftp, names);
                    //getFile(ftp, names);
                    break;
                case "put":
                    //putFile(ftp, names);
                    putDirectory(ftp, names);
                    break;
                case "rmdir":
                    deleteDirectory(ftp, names);
                    break;
                case "quit":
                    disconnectFTP(ftp);
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

    public static void displayLocalFiles(FTPClient ftp){
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

    public static void makeDirectory(FTPClient ftp, String ftpNames){
        if(ftpNames.isEmpty() || ftpNames == null){
            System.out.println("Directory name not specified.");
            return;
        }else{
            String dirName = ftpNames;
            try{
                ftp.makeDirectory(dirName);
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static void changeDirectory(FTPClient ftp, String ftpNames){
        if(ftpNames.isEmpty() || ftpNames == null){
            System.out.println("Directory name not specified.");
            return;
        }else{
            String dirName = ftpNames;
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

    public static void deleteFile(FTPClient ftp, String ftpNames){
        if(ftpNames.isEmpty() || ftpNames == null){
            System.out.println("File or directory name not specified");
        }
        else{
            String fileName = ftpNames;
            try{
                if(ftp.deleteFile(fileName)){
                    System.out.println("File deleted");
                    return;
                }
                else{
                    System.out.println("File not found");
                    return;
                }
            }catch(IOException e){
                System.out.println(e.getMessage());
                return;
            }
        }
    }

    public static void deleteDirectory(FTPClient ftp, String ftpNames){
        if(ftpNames.isEmpty() || ftpNames == null){
            System.out.println("File or directory name not specified");
        }else{
            String dirName = ftpNames;
            try{
                FTPFile[] files = ftp.listDirectories();
                for(FTPFile f : files){
                    if(f.getName().equals(dirName)){
                        if(ftp.removeDirectory(f.getName())){
                            //when directory is empty
                            System.out.println("Directory deleted.");
                        }else{
                            //when the directory is not empty
                            deleteDirectoryContents(ftp, f, "");
                            System.out.println(" " + ftp.getReplyString());
                        }
                    }
                }
            }catch(IOException e){
                System.out.println(e.getMessage());
                return;
            }
        }
    }

    public static void deleteDirectoryContents(FTPClient ftp, FTPFile currentDir, String pathStr){
        try {
            FTPFile[] files = ftp.listFiles(pathStr + "/" + currentDir.getName());
            for(FTPFile f : files){
                if(f.isDirectory()){
                    if(ftp.removeDirectory(pathStr + "/" + currentDir.getName() + "/" + f.getName())){
                        //delete the empty directory if it is empty
                    }else{
                        deleteDirectoryContents(ftp, f, pathStr + "/" + currentDir.getName());
                    }
                }else{
                    ftp.deleteFile(pathStr + "/" + currentDir.getName() + "/" + f.getName());
                }
            }
            ftp.removeDirectory(pathStr + "/" + currentDir.getName());
            return;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    public static void getFile(FTPClient ftp, String ftpNames){
        if(ftpNames.isEmpty() || ftpNames == null){
            System.out.println("File name not specified");
            return;
        }else{
            try{
                File f = new File(ftpNames);
                FileOutputStream fop = new FileOutputStream(f);
                ftp.retrieveFile(ftpNames, fop);
                fop.close();
            }catch(IOException e ){
                System.out.println(e.getMessage());
            }
        }
    }

    public static void putFile(FTPClient ftp, String ftpNames){
        if(ftpNames.isEmpty() || ftpNames == null){
            System.out.println("File name not specified");
        }else{
            File f = new File(ftpNames);
            if(f.exists()){
                try{
                    FileInputStream fileIn = new FileInputStream(f);
                    if(ftp.storeFile(ftpNames, fileIn)){
                        fileIn.close();
                        System.out.println("File uploaded");
                        return;
                    }else{
                        fileIn.close();
                        System.out.println("File upload failed.");
                        return;
                    }
                }catch (IOException e){
                    System.out.println(e.getMessage());
                    return;
                }
            }
            System.out.println("File does not exist locally");
            return;
        }
    }

    public static void putDirectory(FTPClient ftp, String ftpNames){
        try{
            File localCurrentDir = new File(".");
            File[] localDirFiles = localCurrentDir.listFiles();
            for(File f : localDirFiles){
                if(f.getName().equals(ftpNames)){
                    ftp.makeDirectory(f.getName());
                    putDirectoryContents(ftp, f, "" + f.getName());
                    System.out.println(ftp.getReplyString());
                }
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        catch(Exception e2){
            System.out.println(e2.getMessage());
        }
    }

    public static void putDirectoryContents(FTPClient ftp, File currentDir, String pathStr){
        for(File f: currentDir.listFiles()){
            if(f.isDirectory()){
                try {
                    ftp.makeDirectory(pathStr + "/" + f.getName());
                    //ftp.makeDirectory(pathStr + "/" + currentDir.getName() + "/" + f.getName());
                }catch(IOException e){
                    System.out.println(e.getMessage());
                }
                putDirectoryContents(ftp, f, pathStr + "/" + f.getName());
                //putDirectoryContents(ftp, f, pathStr + "/" + currentDir.getName() + "/" + f.getName());
            }else{
                try{
                    FileInputStream fIn = new FileInputStream(f);
                    ftp.storeFile(pathStr + "/" + f.getName(), fIn);
                    fIn.close();
                }catch(IOException e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static void determineFileType(FTPClient ftp, String ftpCommand, String ftpNames){
        try{
            FTPFile[] files = ftp.listFiles();
            for(FTPFile f : files){
                if(f.getName().equals(ftpNames)){
                    if(f.isFile()){
                        switch(ftpCommand){
                            case "put":

                                break;
                            case "get":
                                getFile(ftp, ftpNames);
                                break;
                                default:
                                    break;
                        }
                    }else if(f.isDirectory()){
                    }
                }
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public static void getDirectory(FTPClient ftp, String ftpNames){
        try{
            FTPFile[] files = ftp.listDirectories();
            for(FTPFile f : files){
                if(f.getName().equals(ftpNames)){
                    File currentDir = new File(f.getName());
                    currentDir.mkdir();
                    getDirectoryContents(ftp, f, "");
                    System.out.println(" " + ftp.getReplyString());
                }
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public static void getDirectoryContents(FTPClient ftp, FTPFile currentDir, String pathStr){
        try {
            for(FTPFile f : ftp.listFiles(pathStr + "/" + currentDir.getName())){
                if(f.isDirectory()){
                    File localDir = new File("./" + pathStr + "/" + currentDir.getName() + "/" + f.getName());
                    localDir.mkdir();
                    getDirectoryContents(ftp, f, pathStr + "/" + currentDir.getName());
                }else{
                    File localFile = new File("./" + pathStr + "/" + currentDir.getName() + "/" + f.getName());
                    localFile.createNewFile();
                    FileOutputStream fop = new FileOutputStream(localFile);
                    ftp.retrieveFile(pathStr + "/" + currentDir.getName() + "/" + f.getName(), fop);
                    fop.close();
                }
            }
            return;
        } catch (IOException e) {
            System.out.println(e.getMessage());
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

    public static void disconnectFTP(FTPClient ftp){
        try {
            ftp.disconnect();
            System.out.println("goodbye");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
