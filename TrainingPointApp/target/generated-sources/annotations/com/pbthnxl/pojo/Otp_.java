package com.pbthnxl.pojo;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.10.v20211216-rNA", date="2024-06-16T19:55:50")
@StaticMetamodel(Otp.class)
public class Otp_ { 

    public static volatile SingularAttribute<Otp, Date> otpRequestedTime;
    public static volatile SingularAttribute<Otp, Integer> id;
    public static volatile SingularAttribute<Otp, String> email;
    public static volatile SingularAttribute<Otp, String> oneTimePassword;

}