#import "@preview/citegeist:0.2.0": load-bibliography

#let physical() = context {
    let chapters = query(heading.where(level: 1, outlined: true))
        .filter(it => not it.numbering == none).len()
    let pages = counter(page).final().first()

    let bibtex_string = read("literatura.bib")
    let bib = load-bibliography(bibtex_string)
    let citations = bib.len()

    let tables = query(figure.where(kind: table)).len()
    let images = query(
        figure.where(kind: image).or(figure.where(kind: raw))
    ).len()
    let graphics = 0

    let appendices = query(heading.where(level: 1))
        .filter(x => x.has("label")
                and repr(x.label).starts-with("<dodatak")).len()


  (chapters, pages, citations, tables, images, graphics, appendices).map(str).join("/")
}

#let dodatak(d) = figure(
    kind: "dodatak",
    supplement: [Додатак],
    d
)

// Маркирање потребних измена. Базирано на
// https://github.com/typst/typst/issues/662#issuecomment-1516709607
#let todo(x) = [ #text([TODO: #x <todo>], red) ]

#let todos() = context {
    let elems = query(
        <todo>
    )

    if elems.len() == 0 {
        return
    }

    pagebreak()
    [= TODOs <todos>]

    for body in elems {
        text([+ #link(body.location(), body)], red)
    }
}

// Провера недостајућих поља у референцама
#let checkbib() = context {
    let bibtex_string = read("literatura.bib")
    let bib = load-bibliography(bibtex_string)
    let mandatory_fields = (
        "online": ("title", "year", "url", "urldate"),
        "article": ("author", "year", "title", "journal"),
        "book": ("author", "year", "title", "publisher"),
        "inproceedings": ("author", "year", "title", "booktitle"),
    )
    let errors = ()

    for (key, entry) in bib {
        for (bib_type, fields) in mandatory_fields {
            if entry.at("entry_type") == bib_type {
                for field in fields {
                    if field not in entry.at("fields") {
                        errors.push((key, field))
                    }
                }
            }
        }
    }

    if errors.len() == 0 {
        return
    }

    pagebreak()
    [= Грешке у литературним наводима <refgreske>

     Овде се налази списак грешака у литературним наводима које морате исправити
     у `literatura.bib` фајлу.

    ]

    for (key, field) in errors {
        [+ Референци #raw(key) недостаје поље #raw(field)]
    }
}
