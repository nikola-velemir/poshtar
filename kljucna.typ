// У овом фајлу не треба ништа да мењате.
// Све параметре подешавате у metadata.typ

#import "metadata.typ": *

#let velicina_fonta = if format_strane == "a4" {
     7.5pt
} else {
     6.5pt
}

#set text(size: velicina_fonta, font: "Liberation Sans")
#set rect(inset: 0pt)

// Поставити на другу боју за дебаговање. Нпр. red
#let clr = white
#let left_edge = 36.9%
#let left_edge_eng = 36.7%
#let left_edge_komisija = 36.9%
#let left_edge_eng_komisija = 36.9%

#block({
  image("formulari/Q2_NA_04-05 KDI-crop.pdf", height: 100%)
  place(left + top,
      dy: 16.7%,
      dx: left_edge,
      rect(align(left + horizon, "Монографска документација"),
          fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 19%,
      dx: left_edge,
      rect(align(left + horizon, "Текстуални штампани материјал"),
          fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 21.3%,
      dx: left_edge,
      rect(align(left + horizon, vrsta_rada), fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 23.62%,
      dx: left_edge,
      rect(align(left + horizon, autor), fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 25.92%,
      dx: left_edge,
      rect(align(left + horizon, "Др " + mentor + ", " + mentor_zvanje),
          fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 28.6%,
      dx: left_edge,
      rect(align(left, naslov), fill: clr, width: 59%, height: 27pt)
  )
  place(left + top,
      dy: 33.55%,
      dx: left_edge,
      rect(align(left + horizon, "српски/ћирилица"),
          fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 35.87%,
      dx: left_edge,
      rect(align(left + horizon, "српски/енглески"),
          fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 38.17%,
      dx: left_edge,
      rect(align(left + horizon, "Република Србија"),
          fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 40.55%,
      dx: left_edge,
      rect(align(left + horizon, "Војводина"),
          fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 42.8%,
      dx: left_edge,
      rect(align(left + horizon, godina), fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 45.15%,
      dx: left_edge,
      rect(align(left + horizon, "Ауторски репринт"),
          fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 47.45%,
      dx: left_edge,
      rect(align(left + horizon, "Нови Сад, трг Доситеја Обрадовића 6"),
          fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 50.1%,
      dx: left_edge,
      rect(align(left + horizon, fizicki_opis), fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 52.75%,
      dx: left_edge,
      rect(align(left + horizon, oblast), fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 55.1%,
      dx: left_edge,
      rect(align(left + horizon, disciplina), fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 57.9%,
      dx: left_edge,
      rect(align(left + horizon, kljucne_reci), fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 63.5%,
      dx: left_edge,
      rect(align(left + horizon, "У библиотеци Факултета техничких наука, Нови Сад"),
          fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 69.8%,
      dx: left_edge,
      rect(align(left, apstrakt), fill: clr, width: 59%, height: 14%)
  )
  place(left + top,
      dy: 86.65%,
      dx: left_edge,
      rect(align(left + horizon, datum_odbrane), fill: clr, width: 59%, height: 12pt)
  )

  // Комисија
  place(left + top,
      dy: 88.95%,
      dx: left_edge_komisija,
      rect(align(left + horizon, "Др " + komisija_predsednik + ", " + komisija_predsednik_zvanje),
          fill: clr, width: 38%, height: 12pt)
  )
  place(left + top,
      dy: 91.25%,
      dx: left_edge_komisija,
      rect(align(left + horizon, "Др " + komisija_clan + ", " + komisija_clan_zvanje),
          fill: clr, width: 38%, height: 12pt)
  )
  place(left + top,
      dy: 95.91%,
      dx: left_edge_komisija,
      rect(align(left + horizon, "Др " + mentor + ", " + mentor_zvanje),
          fill: clr, width: 38%, height: 12pt)
  )

})

#pagebreak()

#block({
  image("formulari/Q2_NA_04-05 KDI-crop.pdf", height: 100%, page: 2)
  place(left + top,
      dy: 15.52%,
      dx: left_edge_eng,
      rect(align(left + horizon, "Monographic publication"),
          fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 17.85%,
      dx: left_edge_eng,
      rect(align(left + horizon, "Textual printed material"),
          fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 22.5%,
      dx: left_edge_eng,
      rect(align(left + horizon, autor_eng), fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 24.8%,
      dx: left_edge_eng,
      rect(align(left + horizon, mentor_eng + ", Phd., " + mentor_zvanje_eng),
          fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 27.4%,
      dx: left_edge_eng,
      rect(align(left, naslov_eng), fill: clr, width: 59%, height: 27pt)
  )
  place(left + top,
      dy: 32.4%,
      dx: left_edge_eng,
      rect(align(left + horizon, "Serbian"),
          fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 34.7%,
      dx: left_edge_eng,
      rect(align(left + horizon, "Serbian/English"),
          fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 37.05%,
      dx: left_edge_eng,
      rect(align(left + horizon, "Republic of Serbia"),
          fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 39.4%,
      dx: left_edge_eng,
      rect(align(left + horizon, "Vojvodina"),
          fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 41.7%,
      dx: left_edge_eng,
      rect(align(left + horizon, godina), fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 44.05%,
      dx: left_edge_eng,
      rect(align(left + horizon, "Author's reprint"),
          fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 46.35%,
      dx: left_edge_eng,
      rect(align(left + horizon, "Novi Sad, Dositeja Obradovica sq. 6"),
          fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 49%,
      dx: left_edge_eng,
      rect(align(left + horizon, fizicki_opis), fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
    dy: 51.65%,
    dx: left_edge_eng,
    rect(align(left + horizon, oblast_eng), fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 53.95%,
      dx: left_edge_eng,
      rect(align(left + horizon, disciplina_eng), fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 56.9%,
      dx: left_edge_eng,
      rect(align(left + horizon, kljucne_reci_eng), fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
      dy: 62.35%,
      dx: left_edge_eng,
      rect(align(left + horizon, "The Library of Faculty of Technical Sciences, Novi Sad"),
          fill: clr, width: 59%, height: 12pt)
  )
  place(left + top,
    dy: 68.8%,
    dx: left_edge_eng,
    rect(align(left, apstrakt_eng), fill: clr, width: 59%, height: 14%)
  )
  place(left + top,
      dy: 86.35%,
    dx: left_edge,
    rect(align(left + horizon, datum_odbrane), fill: clr, width: 59%, height: 12pt)
  )

  // Комисија
  place(left + top,
      dy: 88.7%,
      dx: left_edge_eng_komisija,
      rect(align(left + horizon, komisija_predsednik_eng
      + ", Phd., " + komisija_predsednik_zvanje_eng),
          fill: clr, width: 38%, height: 12pt)
  )
  place(left + top,
      dy: 91%,
      dx: left_edge_eng_komisija,
      rect(align(left + horizon, komisija_clan_eng + ", Phd., " + komisija_clan_zvanje_eng),
          fill: clr, width: 38%, height: 12pt)
  )
  place(left + top,
      dy: 95.65%,
      dx: left_edge_eng_komisija,
      rect(align(left + horizon, mentor_eng + ", Phd., " + mentor_zvanje_eng),
          fill: clr, width: 38%, height: 12pt)
  )
})
