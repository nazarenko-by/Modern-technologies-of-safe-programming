public class Main {
    public static void main(String[]args){
        BCrypt b = new BCrypt();
        String salt = BCrypt.gensalt();
        String hash = BCrypt.hashpw(args[1], salt);
        System.out.println(hash); 
    }
    
}
