package com.vnstock.kbs.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Company profile information from KBS.
 * 
 * @param symbol Stock symbol
 * @param businessModel Business model description
 * @param foundedDate Company founded date
 * @param charterCapital Charter capital
 * @param numberOfEmployees Number of employees
 * @param listingDate Listing date on exchange
 * @param parValue Par value per share
 * @param exchange Stock exchange
 * @param listingPrice Listing price
 * @param listedVolume Listed volume
 * @param ceoName CEO name
 * @param ceoPosition CEO position
 * @param inspectorName Inspector name
 * @param inspectorPosition Inspector position
 * @param establishmentLicense Establishment license number
 * @param businessCode Business registration code
 * @param taxId Tax ID
 * @param auditor Auditor name
 * @param companyType Company type
 * @param address Company address
 * @param phone Phone number
 * @param fax Fax number
 * @param email Email address
 * @param website Company website
 * @param freeFloatPercentage Free float percentage
 * @param freeFloat Free float shares
 * @param outstandingShares Outstanding shares
 * @param asOfDate Data as of date
 * @param subsidiaries List of subsidiary companies
 * @param leaders List of company leaders
 * @param ownership Ownership structure
 * @param shareholders Major shareholders
 * @param charterCapitalHistory Charter capital history
 */
public record CompanyProfile(
    @JsonProperty("SB") String symbol,
    @JsonProperty("SM") String businessModel,
    @JsonProperty("FD") String foundedDate,
    @JsonProperty("CC") BigDecimal charterCapital,
    @JsonProperty("HM") Integer numberOfEmployees,
    @JsonProperty("LD") String listingDate,
    @JsonProperty("FV") BigDecimal parValue,
    @JsonProperty("EX") String exchange,
    @JsonProperty("LP") BigDecimal listingPrice,
    @JsonProperty("VL") Long listedVolume,
    @JsonProperty("CTP") String ceoName,
    @JsonProperty("CTPP") String ceoPosition,
    @JsonProperty("IS") String inspectorName,
    @JsonProperty("ISP") String inspectorPosition,
    @JsonProperty("FP") String establishmentLicense,
    @JsonProperty("BP") String businessCode,
    @JsonProperty("TC") String taxId,
    @JsonProperty("KT") String auditor,
    @JsonProperty("TY") String companyType,
    @JsonProperty("ADD") String address,
    @JsonProperty("PHONE") String phone,
    @JsonProperty("FAX") String fax,
    @JsonProperty("EMAIL") String email,
    @JsonProperty("URL") String website,
    @JsonProperty("KLCPNY") BigDecimal freeFloatPercentage,
    @JsonProperty("SFV") Long freeFloat,
    @JsonProperty("KLCPLH") Long outstandingShares,
    @JsonProperty("AD") String asOfDate,
    @JsonProperty("Subsidiaries") List<Subsidiary> subsidiaries,
    @JsonProperty("Leaders") List<Leader> leaders,
    @JsonProperty("Ownership") List<Ownership> ownership,
    @JsonProperty("Shareholders") List<Shareholder> shareholders,
    @JsonProperty("CharterCapital") List<CharterCapitalEntry> charterCapitalHistory
) {
    
    /**
     * Subsidiary company information.
     */
    public record Subsidiary(
        @JsonProperty("D") String updateDate,
        @JsonProperty("NM") String name,
        @JsonProperty("CC") BigDecimal charterCapital,
        @JsonProperty("OR") BigDecimal ownershipPercent,
        @JsonProperty("CR") String currency
    ) {}
    
    /**
     * Company leader information.
     */
    public record Leader(
        @JsonProperty("FD") String fromDate,
        @JsonProperty("PN") String position,
        @JsonProperty("NM") String name,
        @JsonProperty("PO") String positionEn,
        @JsonProperty("PI") String ownerCode
    ) {}
    
    /**
     * Ownership structure entry.
     */
    public record Ownership(
        @JsonProperty("NM") String ownerType,
        @JsonProperty("OR") BigDecimal ownershipPercentage,
        @JsonProperty("SH") Long sharesOwned,
        @JsonProperty("D") String updateDate
    ) {}
    
    /**
     * Major shareholder information.
     */
    public record Shareholder(
        @JsonProperty("NM") String name,
        @JsonProperty("D") String updateDate,
        @JsonProperty("V") Long sharesOwned,
        @JsonProperty("OR") BigDecimal ownershipPercentage
    ) {}
    
    /**
     * Charter capital history entry.
     */
    public record CharterCapitalEntry(
        @JsonProperty("D") String date,
        @JsonProperty("V") BigDecimal charterCapital,
        @JsonProperty("C") String currency
    ) {}
}
