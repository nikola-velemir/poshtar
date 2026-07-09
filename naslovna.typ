// У овом фајлу не треба ништа да мењате.
// Све параметре подешавате у metadata.typ

#import "metadata.typ": *

#set page(
    margin: (left: 20mm, right: 20mm, top: 20mm, bottom: 20mm),
    numbering: none
  )
#grid(columns: (auto, 60%, auto), gutter: 2mm,
    align(left, image("logo/uns-logo.svg", width: 70%)),
    align(center + horizon, text([УНИВЕРЗИТЕТ У НОВОМ САДУ \
    *ФАКУЛТЕТ ТЕХНИЧКИХ НАУКА У
    НОВОМ САДУ*], size: 16pt)),
    align(right, image("logo/ftn-logo.svg", width: 70%)),
)
#line(length: 100%)

#v(3cm)

#align(center, text(if naslov == "Шаблон и упутство за писање завршних радова"
          {  } else { autor },
          size: 20pt))

#v(1cm)

#align(center, text(strong(naslov), size: 25pt))

#v(1cm)

#if naslov != "Шаблон и упутство за писање завршних радова" {
    align(center, text("ЗАВРШНИ РАД", size: 20pt))
    align(center, text(stepen, size: 20pt))
}



#align(center + bottom, text([Нови Сад, #godina], size: 18pt))
