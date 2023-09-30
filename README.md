[![Powered By Vaadin on Kotlin](http://vaadinonkotlin.eu/iconography/vok_badge.svg)](http://vaadinonkotlin.eu)
[![Join the chat at https://gitter.im/vaadin/vaadin-on-kotlin](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/vaadin/vaadin-on-kotlin?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

# Beverage Buddy App Starter for Vaadin
:coffee::tea::sake::baby_bottle::beer::cocktail::tropical_drink::wine_glass:

This is a [Vaadin](https://vaadin.com/) example application.
A full-stack app: uses the H2 database instead of a dummy service; uses [JOOQ](https://www.jooq.org/)
to access the database. Requires Java 17+. Does not run on Spring.

The Starter demonstrates the core Vaadin Flow concepts:
* [Building UIs in Kotlin](https://github.com/mvysny/karibu-dsl) with components
  such as `TextField`, `Button`, `ComboBox`, `DatePicker`, `VerticalLayout` and `Grid` (see `CategoriesList`)
* [Creating forms with `Binder`](https://github.com/vaadin/free-starter-flow/blob/master/documentation/using-binder-in-review-editor-dialog.asciidoc) (`ReviewEditorDialog`)
* Making reusable Components on server side with `KComposite` (`AbstractEditorDialog`)
* [Creating Navigation with the Router API](https://github.com/vaadin/free-starter-flow/blob/master/documentation/using-annotation-based-router-api.asciidoc) (`MainLayout`, `ReviewsList`, `CategoriesList`)
* [Browserless testing](https://github.com/mvysny/karibu-testing): see the
  [test suite package](src/test/kotlin/com/vaadin/starter/beveragebuddy/ui) for the complete test implementation.

This version of Beverage Buddy demoes the possibility of developing a Vaadin
web application purely server-side in the Kotlin language. There is no
JavaScript code in this project. If you'd like to see
a demo on how to create Polymer Templates, please head to the page of the
[original Beverage Buddy App](https://github.com/vaadin/beverage-starter-flow) (in Java).

See the [online demo](https://v-herd.eu/beverage-buddy-vok/).

# Documentation

Please see the [Vaadin Boot](https://github.com/mvysny/vaadin-boot#preparing-environment) documentation
on how you run, develop and package this Vaadin-Boot-based app.

## Database

Without the database, we could store the categories and reviews into session only, which would then be gone when the server rebooted.
We will use the JOOQ library to ease access to the SQL. To make things easy we'll
use in-memory H2 database which will be gone when the server is rebooted - *touche* :-D

We will use [Flyway](https://flywaydb.org/) for database migration. Check out [Bootstrap.kt](src/main/kotlin/com/vaadin/starter/beveragebuddy/Bootstrap.kt)
on how the [migration scripts](src/main/resources/db/migration) are ran when the app is initialized.

JOOQ records are generated in the [com.vaadin.starter.beveragebuddy.backend.jooq](src/main/kotlin/com/vaadin/starter/beveragebuddy/backend/jooq/)
package, including the `CategoryRecord` and `ReviewRecord`. See `JooqGenerator` on how to
generate the records. There are no DTOs - the records are passed around in the app and edited directly in the forms.
The records therefore contain jakarta.validation annotations to validate user input.
Careful when re-generating records, since those annotations will be lost.

The [simplejooq](src/main/kotlin/com/vaadin/starter/beveragebuddy/backend/simplejooq/)
package contains useful functions to access database without Spring:

* The `db{}` function runs given block in a transaction. You can't go simpler than that.
* The `DAO` collection of extension functions, allowing you to write `CATEGORY.getById()` and similar.
* Validations
