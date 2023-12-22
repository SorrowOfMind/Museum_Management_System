package com.museum.models;

import java.io.Serializable;
import java.sql.Date;

public class Worker implements Serializable {
    private static final long serialVersionUID = 6529685098267757666L;

    public int workerID;
    public String name ;
    public String surname;
    private Date dateOfBirth;
    private String phone;
    private String email;
    private Date terminationDate;
    private String agreementType;
    private Date agreementDate;
    private String accountNumber;
    private int salary;
    private String jobTitle;

    public Worker(
            int workerID,
            String name,
            String surname,
            Date dateOfBirth,
            String phone,
            String email,
            Date terminationDate,
            String agreementType,
            Date agreementDate,
            String accountNumber,
            int salary,
            String jobTitle
    ) {
        this.workerID = workerID;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.email = email;
        this.terminationDate = terminationDate;
        this.agreementType = agreementType;
        this.agreementDate = agreementDate;
        this.accountNumber = accountNumber;
        this.salary = salary;
        this.jobTitle = jobTitle;
    }

    public Worker(
            String name,
            String surname,
            Date dateOfBirth,
            String phone,
            String email,
            Date terminationDate,
            String agreementType,
            Date agreementDate,
            String accountNumber,
            int salary,
            String jobTitle
    ) {
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.email = email;
        this.terminationDate = terminationDate;
        this.agreementType = agreementType;
        this.agreementDate = agreementDate;
        this.accountNumber = accountNumber;
        this.salary = salary;
        this.jobTitle = jobTitle;
    }

    public int getWorkerID() {
        return workerID;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPhone(){
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public Date getTerminationDate() {
        return terminationDate;
    }

    public String getAgreementType() {
        return agreementType;
    }

    public Date getAgreementDate() {
        return agreementDate;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public int getSalary() {
        return salary;
    }

    public String getJobTitle() {
        return jobTitle;
    }

}
