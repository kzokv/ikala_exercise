package org.ikala.manager;


import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MavenArgsManager {
    /**
     * Define maven arguments in command line in below sction
     * Naming convention below:
     * NAME: MVN_ARG_<NAME YOUR ARGUMENT>
     * VALUE: ARG Name after -D used in maven command line. Use dot as separator.
     *
     * e.g. -DserviceEndPointUrl where "serviceEndPointUrl" is your value
     */
    private final String MVN_ARG_EMAIL = "email";
    private final String MVN_ARG_PASSWORD = "password";
    private final String MVN_ARG_SECRET_GOOGLE_AUTH_KEY = "secretGoogleAuthKey";


    /**
     * Define variable that stores the value from maven arguments
     * Naming convention should be exactly same as ArgName value by removing dot
     * e.g.
     * The value of MVN_ARG_OS_NAME = test.os.name
     * The variable name of storing value of MVN_ARG_OS_NAME from System.getProperty() should be testOsName
     */
    private String email;
    private String password;
    private String secretGoogleAuthKey;



    /**
     * Other variables
     */
    private final String STR_EMPTY = "";
    private final String STR_BLANK = " ";
    private final String STR_SEPARATOR = "\\.";
    private final String STR_MVN_ARG_STARTS = "MVN_ARG_";

    public MavenArgsManager() throws IllegalAccessException {
        setRuntimeValForAll();
    }

    /**
     * Assign values of maven argument to corresponding variables with help of reflect
     * @throws IllegalAccessException
     */
    private void setRuntimeValForAll() throws IllegalAccessException {
        Field[] fields = this.getClass().getDeclaredFields();
        //Modifier 18 is a final one
        List<Field> finalFields = Arrays.stream(fields).filter(field -> field.getModifiers()== 18 && field.getName().startsWith(STR_MVN_ARG_STARTS)).collect(Collectors.toList());
        List<Field> nonFinalFields = Arrays.stream(fields).filter(field -> field.getModifiers() != 18).collect(Collectors.toList());

        for (Field field : finalFields) {
            field.setAccessible(true);
            String propertyKey = (String) field.get(this);
            Field assignToField = nonFinalFields.stream()
                    .filter(field1 -> field1.getName().toLowerCase().contains(propertyKey.toLowerCase().replaceAll(STR_SEPARATOR, STR_EMPTY)))
                    .findFirst()
                    .orElse(null);

            String propertyValOfTheKey = System.getProperty(propertyKey);

            if (assignToField != null) {
                assignToField.setAccessible(true);
                assignToField.set(this, propertyValOfTheKey);
                assignToField.setAccessible(false);
            }
            field.setAccessible(false);
        }
    }

    //=================getters================================
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getSecretGoogleAuthKey() {
        return secretGoogleAuthKey.trim().replaceAll(STR_BLANK,STR_EMPTY);
    }
}
