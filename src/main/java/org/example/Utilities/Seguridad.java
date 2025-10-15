package org.example.Utilities;

import org.mindrot.jbcrypt.BCrypt;

public class Seguridad {
    // Hashea una contraseña
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Verifica si una contraseña coincide con su hash
    public static boolean checkPassword(String password, String hash){
        return BCrypt.checkpw(password, hash);
    }

}
