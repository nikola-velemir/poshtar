#let format_strane = "a4"         // могуће вредности: iso-b5, a4
#let outline_num = 4
#let naslov = "Имплементација Mediator софверског обрасца за Java екосистеме"
#let autor = "Никола Велемир"

// На енглеском
#let naslov_eng = "Implementation of Mediator design pattern for Java ecosystems"
#let autor_eng = "Nikola Velemir"

#let indeks = "SV 8/2022"

// Име и презиме ментора
#let mentor = "Никола Лубурић"
// Звање: редовни професор, ванредни професор, доцент
#let mentor_zvanje = "ванредни професор"

// Скинути коментаре са одговарајућих линија
#let studijski_program = "Софтверско инжењерство и информационе технологије"
//#let studijski_program = "Рачунарство и аутоматика"
// #let stepen = "Мастер академске студије"
#let stepen = "Основне академске студије"

#let godina = [#datetime.today().year()]

#let kljucne_reci = "апликације, софтверски обрасци, архитектура софтвера, софтверска библиотека, Mediator, дизајн, Java, Spring Framework, Google Guice, Quarkus Framework"
#let apstrakt = [
  Рад се бави проблемом архитектуре апликација пословног домена унутар _Java_ екосистема. У раду је представљен софтверски образац _Mediator_ који омогућава лакше одржавање и проширивање апликација, који је срж библиотеке у питању. У раду је представљена имплементација и архитектура библиотеке, као и начини на које се она интегрише у постојеће алате унутар _Java_ екосистема. Резултат је софтверско решење које унапређује архитектуру апликација пословног домена и омогућава лакше одржавање и проширивање апликација, путем униформног начина коришћења који библиотека диктира.
]

// На енглеском
#let kljucne_reci_eng = "applications, software patterns, software architecture, software library, Mediator, design, Java, Spring Framework, Google Guice, Quarkus Framework"
#let apstrakt_eng = [
 This thesis addresses the problem of business domain application architecture within the _Java_ ecosystem. It presents the _Mediator_ software pattern, which facilitates easier maintenance and extension of applications and forms the core of the library in question. The paper covers the implementation and architecture of the library, as well as the ways it integrates into existing tools within the _Java_ ecosystem. The result is a software solution that enhances business domain application architecture and enables easier application maintenance and expansion through a uniform usage model dictated by the library.
]

// TODO: Текст задатка добијате од ментора. Заменити доле #lorem(100) са текстом задатка.
#let zadatak = [
1. Изучити постојеће архитектуре апликација унутар _Java_ екосистема и идентификовати проблеме у њиховом одржавању и проширивању.
2. Истражити софтверски образац _Mediator_ и његову примену у архитектури апликација.
4. Упознати се са постојећим решењима и библиотекама које користе _Mediator_ образац у _Java_ екосистему, као и са њиховим предностима и манама.
3. Дизајнирати и имплементирати софтверску библиотеку која користи _Mediator_ образац за побољшање архитектуре апликација унутар _Java_ екосистема.
5. Адресирати проблеме и изазове постојећих решења, те наћи начин да се они реше у оквиру нове библиотеке.
6. Документовати пројектантксе шаблоне и представити како су они интегрисани у библиотеку.
]

// TODO: Датум одбране и чланове комисије добијате од ментора
#let datum_odbrane = "10.09.2026"
#let komisija_predsednik = "Игор Дејановић"
#let komisija_predsednik_zvanje = "редовни професор"
#let komisija_clan = "Гордана Милосављевић"
#let komisija_clan_zvanje = "редовни професор"

// На енглеском уписати чланове на латиници
#let komisija_predsednik_eng = "Igor Dejanović"
#let komisija_clan_eng = "Gordana Milosavljević"
#let mentor_eng = "Nikola Luburić"


// Ово даље углавном не треба мењати.

#let zvanje_eng = (
  "редовни професор": "full professor",
  "ванредни професор": "assoc. professor",
  "доцент": "asist. professor",
)
#let komisija_predsednik_zvanje_eng = zvanje_eng.at(komisija_predsednik_zvanje)
#let komisija_clan_zvanje_eng = zvanje_eng.at(komisija_clan_zvanje)
#let mentor_zvanje_eng = zvanje_eng.at(mentor_zvanje)


#let vrsta_rada = if stepen == "Мастер академске студије" {
  "Дипломски - мастер рад"
} else {
  "Дипломски - бечелор рад"
}

#let oblast = "Електротехничко и рачунарско инжењерство"
#let oblast_eng = "Electrical and Computer Engineering"
#let disciplina = "Примењене рачунарске науке и информатика"
#let disciplina_eng = "Applied computer science and informatics"

#import "funkcije.typ": *
// Поглавља/страна/цитата/табела/слика/графика/прилога
#let fizicki_opis = physical()
