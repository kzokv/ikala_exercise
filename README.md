- Prerequisites
  - Register google account to Google Authenticator
    - How to setup Google Authenticator:  
      https://support.google.com/accounts/answer/1066447?hl=en&ref_topic=2954345
    - How to get key instead of scanning bar code:  
      https://stackoverflow.com/questions/47267583/two-factor-authentication-with-google-authenticator-manually-type-key-instead
  - Copy the secret key
- Run below maven command
  ```
  mvn clean test -DsuiteXmlFile=<xml file> -Demail=<your email> -Dpassword=<your password> -DsecretGoogleAuthKey=<secret key>
  
  e.g.
  mvn clean test -DsuiteXmlFile=ikala.xml -Demail=****@xxx.com -Dpassword=****** "-DsecretGoogleAuthKey=5yz5 qibs y7dv qggn 2oov 5ikz repc xo7e"
  
  note: it doesn't matter that your secretGoogleAuthKey contains spaces
  ```
- Recording:  
  ![clip](./src/main/resources/ikala_exercise.gif)


