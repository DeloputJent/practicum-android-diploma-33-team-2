package ru.practicum.android.diploma.domain.detail.model

import android.text.Html

data class VacancyDetail (
    val id : String = "",// "0034a1ac-31d2-329f-8141-fa66b17f3ca8",
    val vacancyName : String = "", //""name": "DevOps-инженер",
    val salaryCurrency : String = "",
    val salaryRange : String = "", //"salary": {"id": "10","currency": "KGT","from": 120000,"to": 220000},
    val address : String = "", //"address": {"id": "0","city": "Москва","street": "Тверская","building":"raw": "Москва, Тверская, 1"},
    val experience : String = "",//"experience":{"id": "between1And3","name": "От 1 года до 3 лет"},
    val schedule : String = "",//"schedule": {"id": "remote","name": "Удаленная работа"},
    val employment : String = "",//"employment": {"id": "full","name": "Полная занятость"},
    val contactsName : String = "",
    val contactsEmail : String = "",
    val phonesComment : List<String?>,
    val phones : List<String?>,//"contacts": {"id": "3","name": "Смирнов Алексей Иванович","email": "123@gmail.com","phones": [{"comment": null,"formatted": "+7 (999) 456-78-90"},{"comment": null,"formatted": "+7 (999) 654-32-10"}]},
    val description : Html,
    val employerName : String = "",
    val employerLogo : String = "",
    val area : String = "",
    val areas : List<String?>,
    val skills : List<String?>,
    val url : String = "",
    val industry : String = "",
)
