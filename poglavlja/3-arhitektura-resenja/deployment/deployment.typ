== Дистрибуција, објављивање и доступност пројекта

Како би се библиотеци _PoshtaR_ омогућила интеграција у софтверске пројекте и обезбедила доступност целокупној _Java_ заједници, пројекат је објављен као софтвер отвореног кода (енгл. _Open Source_) и дистрибуиран путем централног _Maven_ складишта (_Maven Central_) @mavenapache @sonatype_maven_central. Пројекат је лиценциран под _GNU Lesser General Public License v3.0 (LGPL-3.0)_ лиценцом, која корисницима омогућава слободно коришћење и уграђивање библиотеке у власнички или комерцијални софтвер без обавезе да се изворни код крајњег система учини отвореним @gnu_lgpl_v3.

=== Изворни код и репозиторијум

Репозиторијум са целокупним изворним кодом, верзионисањем и праћењем измена смештен је на платформи _GitHub_, на адреси #link("https://github.com/nikola-velemir/poshtar") @github_platform. Идеја је да се у будућности, уколико се дедукује да пројекат носи вредност по _Java_ заједницу, омогући контрибуисање од стране инжењера или хобиста који су заинтересовани за даљи развој библиотеке.

=== Координате и структурираност Maven пакета

Библиотека је организована кроз вишемодулску (енгл. _multi-module_) _Maven_ архитектуру под групом `io.github.nikola-velemir` @mavenapache. Важно је истаћи да су сви модули јасно раздвојени како би корисници у своје апликације интегрирали само оне компоненте које су им заиста неопходне:

- poshtar-core: Централни модул који садржи компоненте језгра @језгро-библиотеке @документација-језгра. Независан је од било ког специфичног радног оквира. Развојни инжењер може искључиво компоненте библиотеке да интегрише на начин приказан на листингу @listing-core-dependency.

#figure(
  ```xml
  <dependency>
      <groupId>io.github.nikola-velemir</groupId>
      <artifactId>poshtar-core</artifactId>
      <version>{version}</version>
  </dependency>
  ```,
  caption: [Начин интеграције језгра библиотеке унутар пројекта],
) <listing-core-dependency>


- poshtar-spring: Представља наменски адаптер за радни оквир _Spring_ @spring-адаптер. Развојни инжењер може да интегрише овај адаптерски модул, који пружа функционисање библиотеке у поменутом радном оквиру, на начин како је приказанано на листингу @listing-spring-dependency.

#figure(
  ```xml
  <dependency>
      <groupId>io.github.nikola-velemir</groupId>
      <artifactId>poshtar-spring</artifactId>
      <version>{version}</version>
  </dependency>
  ```,
  caption: [Начин интеграције Spring адаптера унутар пројекта],
) <listing-spring-dependency>

- poshtar-guice: Адаптер омогућава интеграцију библиотеке са _Google Guice_ оквиром @guice-адаптер. Развојни инжењер уводи овај адаптер као зависност, како је приказано на листинуг @listing-guice-dependency.

#figure(
  ```xml
  <dependency>
      <groupId>io.github.nikola-velemir</groupId>
      <artifactId>poshtar-guice</artifactId>
      <version>{version}</version>
  </dependency>
  ```,
  caption: [Начин интеграције Guice адаптера унутар пројекта],
) <listing-guice-dependency>

- poshtar-quarkus: Радна екстензија за Quarkus оквир која омогућава аутоматску регистрацију и повезивање свих компоненти @quarkus-адаптер. Раздвојена је на два подмодула у складу са конвенцијама Quarkus екосистема:
  - *Модул времена извршавања (`poshtar-quarkus`)*: Примарна зависност коју развојни инжењер декларише у апликацији, како је приказано на листингу @listing-quarkus-dependency. Омогућава функционисање компоненти библиотеке у времену извршавања @quarkus-адаптер.
  - *Модул постављања (`poshtar-quarkus-deployment`)*: Садржи логику за аугментацију и генерисање кода у фази изградње @quarkus-адаптер. Quarkus га аутоматски повлачи путем механизма екстензија @quarkus. Његова конфигурација (уколико се развија сопствена екстензија) приказана је на листингу @listing-quarkus-deployment-dependency.

#figure(
  ```xml
  <dependency>
      <groupId>io.github.nikola-velemir</groupId>
      <artifactId>poshtar-quarkus</artifactId>
      <version>{version}</version>
  </dependency>
  ```,
  caption: [Начин интеграције _Quarkus runtime_ екстензије унутар пројекта],
) <listing-quarkus-dependency>

#figure(
  ```xml
  <dependency>
      <groupId>io.github.nikola-velemir</groupId>
      <artifactId>poshtar-quarkus-deployment</artifactId>
      <version>{version}</version>
  </dependency>
  ```,
  caption: [Начин интеграције _Quarkus deployment_ модула],
) <listing-quarkus-deployment-dependency>

- poshtar-validator-api: Садржи анотације неопходне за манипулацију валидационим правилима валидаторског модула @валидаторски-модул-сегмент-јавног-програмског-интерфејса. Интегрише се унутар пројекта на начин приказан на листингу @listing-validator-api-dependency.

#figure(
  ```xml
  <dependency>
      <groupId>io.github.nikola-velemir</groupId>
      <artifactId>poshtar-validator-api</artifactId>
      <version>{version}</version>
  </dependency>
  ```,
  caption: [Начин интеграције модула јавног програмског интерфејса за валидацију унутар пројекта],
) <listing-validator-api-dependency>

- poshtar-validator: Модул валидационог процесора анотација који служи за верификацију и валидацију архитектонских правила у времену превођење апликације. Начин интеграције валидационог процесора описан је у погављу @валидаторски-модул-извршни-сегмент.

