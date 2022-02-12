"# hosting-image" 



### Add cert to JRE  

>keytool -import -alias example -keystore "C:\Program Files\Java\jdk-11.0.12\lib\security\cacerts" -file I:\hosting-image\cert\tinify.cer

By default the cacerts keystore has a password of changeit.  

### To delete cert from JRE

>keytool -delete -alias example -keystore "C:\Program Files\Java\jdk-11.0.12\lib\security\cacerts"