package com.piggymetrics.account.domain;


import java.util.List; 
import java.util.Date;

public class StatAccount {

	private String name;
	private List<Item> incomes;
	private List<Item> expenses;
	private StatSaving saving;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Item> getIncomes() {
		return incomes;
	}

	public void setIncomes(List<Item> incomes) {
		this.incomes = incomes;
	}

	public List<Item> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<Item> expenses) {
		this.expenses = expenses;
	}

	public StatSaving getSaving() {
		return saving;
	}

	public void setSaving(StatSaving saving) {
		this.saving = saving;
	}
}
