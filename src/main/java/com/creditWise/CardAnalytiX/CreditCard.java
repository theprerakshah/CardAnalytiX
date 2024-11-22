package com.creditWise.CardAnalytiX;

public class CreditCard
{
	String	cardName;
	String	cardType;
	String	annualFee;
	String	purchaseInterestRate;
	String	additionalFeatures;

	public CreditCard(String cardName, String cardType, String annualFee, String purchaseInterestRate, String additionalFeatures)
	{
		super();
		this.cardName = cardName;
		this.cardType = cardType;
		this.annualFee = annualFee;
		this.purchaseInterestRate = purchaseInterestRate;
		this.additionalFeatures = additionalFeatures;
	}

	public String getCardName()
	{
		return cardName;
	}

	public void setCardName(String cardName)
	{
		this.cardName = cardName;
	}

	public String getCardType()
	{
		return cardType;
	}

	public void setCardType(String cardType)
	{
		this.cardType = cardType;
	}

	public String getAnnualFee()
	{
		return annualFee;
	}

	public void setAnnualFee(String annualFee)
	{
		this.annualFee = annualFee;
	}

	public String getPurchaseInterestRate()
	{
		return purchaseInterestRate;
	}

	public void setPurchaseInterestRate(String purchaseInterestRate)
	{
		this.purchaseInterestRate = purchaseInterestRate;
	}

	public String getAdditionalFeatures()
	{
		return additionalFeatures;
	}

	public void setAdditionalFeatures(String additionalFeatures)
	{
		this.additionalFeatures = additionalFeatures;
	}

}
