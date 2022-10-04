
package br.company.tools;


import org.springframework.security.crypto.password.PasswordEncoder;


public class MyPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence cs) {
        try {
            char[] x = cs.toString().toCharArray();
            return PasswordHash.createHash(x);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    @Override
    public boolean matches(CharSequence cs, String string) {
        try {
            char[] x = cs.toString().toCharArray();
            return PasswordHash.validatePassword(x, string);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return PasswordEncoder.super.upgradeEncoding(encodedPassword);
    }

}
