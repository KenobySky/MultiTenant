package br.company.tools;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;


public class MyTools {

 
    public static String cleanHtml(String descricao) {
        if (descricao == null) {
            descricao = "";
        }
        descricao = Jsoup.clean(descricao, Whitelist.none());
        descricao = descricao.replace("&nbsp;", "");

        return descricao;
    }


  

    public boolean validaEmails(String sendTo) {
        //Verifica o sendTo
        try {
            InternetAddress.parse(sendTo);
        } catch (AddressException ex) {

            return false;
        }
        return true;
    }

 

    public static boolean isEmailValid(String text) {
        String regex = "^(.+)@(.+)$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(text);

        boolean valid = matcher.matches();

        return valid;
    }

    public static boolean validLetter(String value) {
        return Pattern.matches("[a-zA-Z]+", value);
    }

    public static boolean validNumber(String value) {
        return Pattern.matches("[0-9]+", value);
    }

  
    public static String encodeValueToUTF8(String value) {
        try {
            String encode = URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
            encode = encode.replace("+", "%20");
            return encode;
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    public static String txtToHtml(String s) {
        try {
            StringBuilder builder = new StringBuilder();
            boolean previousWasASpace = false;
            for (char c : s.toCharArray()) {
                if (c == ' ') {
                    if (previousWasASpace) {
                        builder.append("&nbsp;");
                        previousWasASpace = false;
                        continue;
                    }
                    previousWasASpace = true;
                } else {
                    previousWasASpace = false;
                }
                switch (c) {
                    case '<':
                        builder.append("&lt;");
                        break;
                    case '>':
                        builder.append("&gt;");
                        break;
                    case '&':
                        builder.append("&amp;");
                        break;
                    case '"':
                        builder.append("&quot;");
                        break;
                    case '\n':
                        builder.append("<br>");
                        break;
                    // We need Tab support here, because we print StackTraces as HTML
                    case '\t':
                        builder.append("&nbsp; &nbsp; &nbsp;");
                        break;
                    default:
                        builder.append(c);

                }
            }
            String converted = builder.toString();
            String str = "(?i)\\b((?:https?://|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:\'\".,<>?������]))";
            java.util.regex.Pattern patt = java.util.regex.Pattern.compile(str);
            java.util.regex.Matcher matcher = patt.matcher(converted);
            converted = matcher.replaceAll("<a href=\"$1\">$1</a>");
            return converted;
        } catch (Exception ex) {
            //ErrorLogger.log(ex);
            ex.printStackTrace();
        }
        return s;
    }




    public static String getTempSaveFileName(String prefix, String extension) {
        String randomWord = "";
        try {
            randomWord = MyTools.generateRandomWords(1)[0];
        } catch (Exception ex) {

            Random rn = new Random();
            randomWord = "" + rn.nextInt(10);
        }

        String saveAt = "";

        try {
            saveAt = System.getProperty("java.io.tmpdir");
        } catch (Exception ex) {

        }

        String saveAs;
        if (prefix != null) {
            saveAs = prefix + "_" + randomWord;
        } else {
            saveAs = prefix + randomWord;
        }

        saveAt = saveAt + saveAs + extension;
        return saveAt;
    }

    public static String getLocalHostName() {
        String hostName = "";
        try {
            hostName = InetAddress.getLocalHost().getHostName();

        } catch (UnknownHostException ex) {
            hostName = "";

        }
        return hostName;
    }

    public static String getLocalHostAddress() {
        String hostAddress = "";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();

        } catch (UnknownHostException ex) {
            hostAddress = "";

        }
        return hostAddress;
    }

    public static LocalDate convertSqlDateToLocalDate(java.sql.Date date) {
        if (date == null) {
            return null;
        } else {
            return date.toLocalDate();
        }
    }

    public static java.util.Date convertSqlDateToDate(java.sql.Date sqlDate) {
        java.util.Date javaDate = null;
        if (sqlDate != null) {
            javaDate = new Date(sqlDate.getTime());
        }
        return javaDate;
    }

    public static Calendar toCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static double convertZonedDateTimeToDouble(ZonedDateTime zonedDateTime) {

        long e = zonedDateTime.toInstant().toEpochMilli();
        return Long.valueOf(e).doubleValue();
    }

    public static long convertZonedDateTimeToLong(ZonedDateTime zonedDateTime) {

        long e = zonedDateTime.toInstant().toEpochMilli();

        return e;

    }

    public static ZonedDateTime convertLocalDateToZonedDateTime(LocalDate ld) {
        ZonedDateTime zonedDateTime = ld.atStartOfDay(ZoneId.systemDefault());
        return zonedDateTime;
    }

    public static ZonedDateTime convertLongToZonedDateTime(long e) {

        Instant i = Instant.ofEpochMilli(e);
        ZonedDateTime ofInstant = ZonedDateTime.ofInstant(i, ZoneId.systemDefault());

        return ofInstant;
    }

    public static long convertDateToLong(Date dt) {
        return dt.getTime();

    }

    public static Date convertLongToDate(long longValue) {
        return new Date(longValue);
    }

    public static LocalDate convertLongToLocalDate(long longValue) {
        return Instant.ofEpochMilli(longValue).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime convertLongToLocalDateTime(long longValue) {
        return Instant.ofEpochMilli(longValue).atZone(ZoneId.of("America/Sao_Paulo")).toLocalDateTime();
    }

    public static LocalDateTime convertLongToLocalDateTime(long longValue, ZoneId zoneId) {
        return Instant.ofEpochMilli(longValue).atZone(zoneId).toLocalDateTime();
    }

    public static long convertLocalDateToLong(LocalDate dt) {
        return dt.atStartOfDay(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
    }

    public static java.sql.Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

    public static LocalDate convertToLocalDateViaMilisecond(Date dateToConvert) {
        if (dateToConvert == null) {
            return null;
        }
        try {
            return Instant.ofEpochMilli(dateToConvert.getTime()).atZone(ZoneId.of("America/Sao_Paulo")).toLocalDate();
        } catch (Exception ex) {

        }
        return null;
    }

    public static Map<Currency, Locale> getCurrencyLocaleMap() {
        Map<Currency, Locale> map = new HashMap<>();
        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                Currency currency = Currency.getInstance(locale);
                map.put(currency, locale);
            } catch (Exception e) {
                // skip strange locale
            }
        }
        return map;
    }

    public static Number roundTo2Decimals(double val) {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setDecimalSeparator('.');

        DecimalFormat df2 = new DecimalFormat("###.##", symbols);

        try {
            return df2.parse(df2.format(val)).doubleValue();
        } catch (ParseException ex) {
            Logger.getLogger(MyTools.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return val;
        }

    }

    /**
     *
     * @param i An Integer
     * @return
     */
    public static String numberToAlphabetic(int i) {

        if (i < 0) {
            return "-" + numberToAlphabetic(-i - 1);
        }

        int quot = i / 27;
        int rem = i % 27;
        char letter = (char) ((int) '@' + rem);
        if (quot == 0) {
            return "" + letter;
        } else {
            return numberToAlphabetic(quot - 1) + letter;
        }
    }

    public static String alphabeticToNumber(String str) {
        String c = "?";

        int MAX_ATTEMPTS = 100000;
        int attempts = 0;
        while (attempts < MAX_ATTEMPTS) {

            String x = numberToAlphabetic(attempts);
            if (x.equalsIgnoreCase(str)) {
                c = "" + attempts;
                break;
            }
            attempts++;
        }

        return c;
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public static String cleanString(String string) {
        try {
            string = Normalizer.normalize(string, Normalizer.Form.NFD);
            string = string.replaceAll("[^\\p{ASCII}]", "");
            string = string.trim();
            string = string.replace("\n", "").replace("\r", "");
            //string = StringUtils.normalizeSpace(string);
            string = string.toLowerCase();
        } catch (Exception ex) {
            return "";
        }
        return string;
    }

    public static String[] generateRandomWords(int numberOfWords) {
        String[] randomStrings = new String[numberOfWords];
        Random random = new Random();
        for (int i = 0; i < numberOfWords; i++) {
            char[] word = new char[10]; // words of length  10.
            for (int j = 0; j < word.length; j++) {
                word[j] = (char) ('a' + random.nextInt(26));
            }
            randomStrings[i] = new String(word);
        }
        return randomStrings;
    }

    /**
     * Devolve a data atual em extenso.
     *
     * @return Devolve a data atual em extenso, utiliza DateFormat,
     * df.format(new Date());
     */
    public static String getDataPorExtenso() {
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, new Locale("pt", "BR"));
        return df.format(new Date());
    }

    public static String formatDate(ZonedDateTime ts, String format) {
        try {

            if (ts == null) {
                return "";
            }

            String format1 = ts.format(DateTimeFormatter.ofPattern(format));
            return format1;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";

    }

    /**
     *
     * @param ts
     * @return a data no formato dd/MM/yyyy
     */
    public static String formatDate(Timestamp ts, String format) {
        try {

            if (ts == null) {
                return "";
            }

            SimpleDateFormat df = new SimpleDateFormat(format);
            return df.format(new java.util.Date(ts.getTime()));
        } catch (Exception ex) {

        }
        return "";

    }

    /**
     *
     * @param ts
     * @return a data no formato dd/MM/yyyy
     */
    public static String formatDate(Timestamp ts) {
        try {

            if (ts == null) {
                return "";
            }

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            return df.format(new java.util.Date(ts.getTime()));
        } catch (Exception ex) {

        }
        return "";

    }

    /**
     *
     * @param date
     * @return a data no formato dd/MM/yyyy
     */
    public static String formatDate(LocalDate date) {

        try {

            if (date == null) {
                return "";
            }

            String format = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return format;
        } catch (Exception ex) {
            Logger.getLogger(MyTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String formatDate(LocalDate date, String pattern) {
        try {

            if (date == null) {
                return "";
            }

            String format = date.format(DateTimeFormatter.ofPattern(pattern));
            return format;
        } catch (Exception ex) {
            Logger.getLogger(MyTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String formatDate(Date date, String pattern) {
        try {

            if (date == null) {
                return "";
            }

            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            String format = sdf.format(date);
            return format;
        } catch (Exception ex) {
            Logger.getLogger(MyTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String formatDate(String date, String pattern) {
        try {

            if (date == null) {
                return "";
            }

            SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
            Date parsed = s.parse(date);

            SimpleDateFormat sdf = new SimpleDateFormat(pattern);

            String format = sdf.format(parsed);
            return format;
        } catch (Exception ex) {
            Logger.getLogger(MyTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    /**
     *
     * @param date
     * @param sdf
     * @return a data no formato sdf
     */
    public static String formatDate(Date date, DateFormat sdf) {
        try {

            if (date == null) {
                return "";
            }

            String format = sdf.format((Date) date);
            return format;
        } catch (Exception ex) {
            Logger.getLogger(MyTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    /**
     *
     * @param date
     * @return a data no formato dd/MM/yyyy
     */
    public static String formatDate(java.util.Date date) {

        if (date == null) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return sdf.format(new java.util.Date(date.getTime()));
        } catch (Exception ex) {
            Logger.getLogger(MyTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getCurrentTimeString() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        return dateFormat.format(new Date());
    }

    public static String getMD5Hash(File file) throws IOException, NoSuchAlgorithmException {
        //Use MD5 algorithm
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");

        //Get the checksum
        String checksum = getFileChecksum(md5Digest, file);
        return checksum;
    }

    private static String getFileChecksum(MessageDigest digest, File file) throws IOException {
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        };

        //close the stream; We don't need it now.
        fis.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        //return complete hash
        return sb.toString();
    }

    /**
     *
     * @param userInput A informacao que o usuario digitou.
     * @param compareTo
     * @return true se a string userInput esta contida no objeto "compareTo".
     */
    public static boolean contains(String userInput, Object compareTo) {
        boolean valid = false;
        //
        if (compareTo == null) {
            return false;
        }

        //System.out.println("\n");
        //System.out.println(userInput);
        //System.out.println(compareTo);
        if (compareTo instanceof Boolean) {
            Boolean compareToBoolean = (Boolean) compareTo;

            if (compareToBoolean) {
                if (userInput.equalsIgnoreCase("sim") || userInput.equalsIgnoreCase("aprovado") || userInput.equalsIgnoreCase("OK")) {
                    return true;
                }

            } else {
                if (userInput.equalsIgnoreCase("n�o") || userInput.equalsIgnoreCase("nao") || userInput.equalsIgnoreCase("reprovado") || userInput.equalsIgnoreCase("NOK")) {
                    return true;
                }
            }

        }

        //Compara qnd for String
        if (compareTo instanceof String) {

            //Strings em que ser�o comparadas
            String compareToString = (String) compareTo;

            if (compareToString.contains(userInput)) {
                valid = true;
            }

            if (cleanString(compareToString).contains(cleanString(userInput))) {
                valid = true;
            }

        }

        //Compara qnd for ZonedDateTime
        if (compareTo instanceof ZonedDateTime) {
            //
            ZonedDateTime zdt = (ZonedDateTime) compareTo;

            //
            String toString = zdt.toString();

            //
            if (toString.contains(userInput) || cleanString(toString).contains(cleanString(userInput))) {
                valid = true;
            }

            try {
                String format = zdt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                //
                if (format.contains(userInput) || cleanString(format).contains(cleanString(userInput))) {
                    valid = true;
                }

                //
                String month = zdt.getMonth().toString();
                String day = zdt.getDayOfWeek().toString();

                //
                month = cleanString(month);
                day = cleanString(day);

                //
                if (day.contains(userInput) || month.contains(userInput)) {
                    valid = true;
                }

            } catch (Exception ex) {
                //ErrorLogger.log(ex);
                ex.printStackTrace();
            }

            //
            //
        }

        //Compara qnd for LocalDate
        if (compareTo instanceof LocalDate) {
            LocalDate localDate = (LocalDate) compareTo;

            String toString = localDate.toString();

            if (toString.contains(userInput) || cleanString(toString).contains(cleanString(userInput))) {
                valid = true;
            }

            try {
                String format = localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                if (format.contains(userInput) || cleanString(format).contains(cleanString(userInput))) {
                    valid = true;
                }

                String month = localDate.getMonth().toString();
                String day = localDate.getDayOfWeek().toString();

                month = cleanString(month);
                day = cleanString(day);

                if (day.contains(userInput) || month.contains(userInput)) {
                    valid = true;
                }
            } catch (Exception ex) {
                //ErrorLogger.log(ex);
                ex.printStackTrace();
            }

        }

        //
        return valid;
    }

    public static boolean equals(String userInput, ArrayList<String> compareToList) {
        boolean valid = false;
        //
        if (compareToList == null) {
            return false;
        }

        for (String compareTo : compareToList) {
            if (compareTo instanceof String) {

                //Strings em que ser�o comparadas
                String compareToString = (String) compareTo;

                if (compareToString.equalsIgnoreCase(userInput) || cleanString(compareToString).equalsIgnoreCase(cleanString(userInput))) {
                    valid = true;
                }

                if (valid) {
                    break;
                }
            }
        }

        //
        return valid;
    }

    public static boolean equals(String userInput, Object compareToObject) {
        boolean valid = false;
        //
        if (compareToObject == null) {
            return false;
        }

        if (compareToObject instanceof ArrayList) {
            ArrayList<String> compareToList = (ArrayList<String>) compareToObject;
            for (String compareTo : compareToList) {
                if (compareTo instanceof String) {

                    //Strings em que ser�o comparadas
                    String compareToString = (String) compareTo;

                    if (compareToString.equalsIgnoreCase(userInput)) {
                        valid = true;
                    }

                    if (cleanString(compareToString).equalsIgnoreCase(cleanString(userInput))) {
                        valid = true;
                    }

                    if (valid) {
                        break;
                    }
                }

            }
        } else if (compareToObject instanceof String) {

            //Strings em que ser�o comparadas
            String compareToString = (String) compareToObject;

            if (compareToString.equalsIgnoreCase(userInput)) {
                valid = true;
            }

            if (cleanString(compareToString).equalsIgnoreCase(cleanString(userInput))) {
                valid = true;
            }

        }

        //
        return valid;
    }

    public static java.util.Date convertLocalDateToDate(LocalDate localDate) {
        Date date1 = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return date1;
    }

    public static String jumpLineIfContainsText(String t, String append) {
        if (t.isBlank()) {
            t = append;
        } else {
            if (!t.endsWith("\n")) {
                t = t + "\n" + append;
            } else {
                t = t + append;
            }
        }

        return t;
    }


  
    public static String getPropertiesSystem() {
        Properties p = System.getProperties();

        return p.toString();

    }

    /**
     * Tries to extract a number present on the String
     *
     * @param str
     * @return
     */
    public static Integer extractNumber(String str) {
        try {
            str = str.replaceAll("\\D+", "");

            return Integer.parseInt(str);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return 0;
    }

//    public static TempoExperiencia extractTempoExperiencia(String tempoExperiencia) {
//
//        try {
//            String cleanString = cleanString(tempoExperiencia);
//
//            if (cleanString.contains("mes") || cleanString.contains("meses")) {
//                return TempoExperiencia.MESES;
//            } else if (cleanString.contains("dia")) {
//                return TempoExperiencia.DIAS;
//            } else if (cleanString.contains("ano")) {
//                return TempoExperiencia.ANOS;
//            } else if (cleanString.contains("semana")) {
//                return TempoExperiencia.SEMANAS;
//            } else if (cleanString.contains("horas")) {
//                return TempoExperiencia.HORAS;
//            } else if (cleanString.contains("decadas")) {
//                return TempoExperiencia.DECADAS;
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        return TempoExperiencia.NAO_APLICAVEL;
//
//    }

    /**
     * Trim the string if not null. if null, return ""
     *
     * @return
     */
    public static String trim(String text) {
        if (text == null) {
            return "";
        } else {
            return text.trim();
        }

    }

}
