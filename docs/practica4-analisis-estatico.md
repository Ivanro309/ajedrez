# Practica 4: Herramientas de analisis estatico de codigo

Asignatura: ACCSI  
Curso: 2025/2026  
Proyecto evaluado: ajedrez  
Fecha de realizacion del analisis: 09/05/2026  
Herramienta utilizada: PMD 7.7.0 mediante Maven PMD Plugin 3.26.0  

> Nota de formato para la entrega: este documento es la fuente de trabajo. Para la entrega final debe exportarse a PDF con tipografia Garamond 12 ptos, segun las normas de la practica.

## 1. Objetivo de la practica

El objetivo de esta practica es aplicar una herramienta de analisis estatico sobre un proyecto Java propio, interpretar los resultados obtenidos y relacionarlos con caracteristicas de calidad del software del marco ISO/IEC 25000. En este caso se utiliza PMD, una herramienta que analiza codigo fuente Java a partir de conjuntos de reglas orientadas a detectar posibles defectos, codigo no usado, malas practicas, problemas de mantenibilidad y patrones de bajo rendimiento.

## 2. Descripcion del codigo Java evaluado

El codigo evaluado corresponde a una aplicacion Java de ajedrez organizada en paquetes de modelo, vista y controlador. La parte mas desarrollada actualmente es el modelo del tablero y de las piezas.

Datos del codigo evaluado:

| Dato | Valor |
|---|---:|
| Fecha del analisis | 09/05/2026 |
| Lenguaje | Java |
| Version configurada de compilacion | Java 21 |
| Version de JVM usada en la ejecucion | OpenJDK 25.0.2 |
| Sistema de construccion | Maven |
| Archivos `.java` evaluados | 21 |
| Lineas totales en `src/main/java` | 1145 |

Distribucion por paquetes:

| Paquete | Responsabilidad |
|---|---|
| `com.ajedrez.modelo` | Representacion del tablero, casillas, piezas y color de las piezas. |
| `com.ajedrez.modelo.piezas` | Reglas de movimiento de alfil, caballo, peon, rey, reina y torre. |
| `com.ajedrez.controlador` | Clases base del controlador del juego y del tablero. |
| `com.ajedrez.vista` | Clase base de la vista del tablero. |
| `com.ajedrez` | Clase principal base de la aplicacion. |

Trabajo de codigo realizado antes del analisis:

| Archivo | Actuacion |
|---|---|
| `Torre.java` | Implementada la clase como subclase de `Pieza`, con validacion de movimientos horizontales y verticales, control de piezas intermedias y control de captura de piezas rivales. |
| `Reina.java` | Implementada la clase como subclase de `Pieza`, combinando movimientos rectos y diagonales, con control de camino libre y captura valida. |
| `Rey.java` | Implementada la clase como subclase de `Pieza`, permitiendo un desplazamiento maximo de una casilla en cualquier direccion. |
| `Caballo.java` | Corregida la validacion del movimiento en L y documentado su comportamiento. |
| `Peon.java` | Corregido el avance segun color, el doble avance inicial, el bloqueo frontal y la captura diagonal. Se documenta que no se contempla captura al paso. |
| `package-info.java` | Anadida documentacion de paquetes para explicar la responsabilidad de cada capa del proyecto. |
| Clases esqueleto | Anadida documentacion de proposito y constructores explicitos en clases todavia sin logica propia. |
| `App.java` | Convertida en punto de entrada JavaFX y configurada la ventana principal. |
| `VistaTablero.java` | Implementada la interfaz grafica con tablero 8x8, piezas, seleccion, turnos, mensajes y boton de reinicio. |
| `ControladorJuego.java` | Implementada la coordinacion de tablero, turno actual, seleccion de origen y aplicacion de movimientos. |
| `Tablero.java` | Anadidas operaciones para inicializar piezas, limpiar tablero, colocar piezas y mover validando reglas. |
| `ajedrez.css` | Anadida hoja de estilos JavaFX para la presentacion del tablero y estados visuales. |

## 3. Instalacion y configuracion de PMD

La herramienta se ha integrado a traves de Maven, usando el plugin `maven-pmd-plugin` definido en `pom.xml`.

Configuracion relevante:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-pmd-plugin</artifactId>
    <version>3.26.0</version>
    <configuration>
        <targetJdk>21</targetJdk>
        <failOnViolation>false</failOnViolation>
        <printFailingErrors>true</printFailingErrors>
        <linkXRef>false</linkXRef>
        <excludes>
            <exclude>com/ajedrez/App.java</exclude>
            <exclude>com/ajedrez/vista/**</exclude>
        </excludes>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>check</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

La configuracion indica que PMD analiza el proyecto con objetivo Java 21. La opcion `failOnViolation=false` permite generar el informe aunque se encuentren incidencias; esto es util para una practica academica, porque facilita analizar los resultados sin bloquear automaticamente la construccion.

Se excluyen `App.java` y el paquete `com/ajedrez/vista/**` porque contienen la capa JavaFX. En el entorno usado para la practica, PMD 7.7.0 ejecutado sobre OpenJDK 25.0.2 muestra errores internos al resolver algunas clases JavaFX. La exclusion permite mantener el analisis estable sobre el modelo y los controladores, que son la parte principal de la logica del juego.

## 4. Reglas utilizadas y justificacion

Se ha usado el conjunto de reglas por defecto del Maven PMD Plugin. El fichero generado por Maven se encuentra en:

`target/pmd/rulesets/001-maven-pmd-plugin-default.xml`

Este conjunto de reglas incluye categorias historicamente asociadas a `java-basic`, `java-empty`, `java-imports`, `java-unnecessary` y `java-unusedcode`. La eleccion es adecuada para este proyecto porque el codigo esta en una fase inicial y conviene detectar primero problemas basicos de correccion, codigo muerto, parametros no usados, imports innecesarios y malas practicas simples antes de aplicar reglas mas estrictas de complejidad o diseno.

Reglas y metricas cualitativas seleccionadas:

| Categoria PMD | Reglas representativas | Justificacion | Caracteristicas ISO/IEC 25000 relacionadas |
|---|---|---|---|
| `bestpractices` | `UnusedFormalParameter`, `UnusedLocalVariable`, `UnusedPrivateField`, `UnusedPrivateMethod`, `AvoidUsingHardCodedIP` | Detectan codigo no usado y practicas que pueden generar defectos o acoplamientos innecesarios. | Mantenibilidad, fiabilidad y seguridad. |
| `codestyle` | `EmptyControlStatement`, `UnnecessaryImport`, `UnnecessaryModifier`, `UnnecessaryReturn`, `UselessParentheses` | Ayudan a mantener un codigo claro, legible y menos propenso a errores por ruido sintactico. | Mantenibilidad y usabilidad interna para desarrolladores. |
| `design` | `CollapsibleIfStatements`, `SimplifiedTernary`, `UselessOverridingMethod` | Detectan estructuras que pueden simplificarse y reducen complejidad accidental. | Mantenibilidad y adecuacion funcional indirecta. |
| `errorprone` | `BrokenNullCheck`, `EmptyCatchBlock`, `ReturnFromFinallyBlock`, `UnconditionalIfStatement`, `OverrideBothEqualsAndHashcode` | Buscan patrones con alta probabilidad de producir errores en ejecucion o comportamientos inesperados. | Fiabilidad, mantenibilidad y seguridad. |
| `multithreading` | `AvoidThreadGroup`, `DontCallThreadRun`, `DoubleCheckedLocking` | Detectan usos peligrosos de concurrencia. Aunque el proyecto aun no usa hilos, se mantienen para prevenir errores futuros. | Fiabilidad y eficiencia de desempeno. |
| `performance` | `BigIntegerInstantiation` | Evita patrones de instanciacion ineficientes en tipos numericos concretos. | Eficiencia de desempeno. |

## 5. Secuencia de comandos ejecutados

Los comandos se ejecutaron desde la raiz del proyecto:

```bash
find src/main/java -name '*.java' -print
wc -l src/main/java/module-info.java \
      src/main/java/com/ajedrez/App.java \
      src/main/java/com/ajedrez/controlador/ControladorJuego.java \
      src/main/java/com/ajedrez/controlador/ControladorTablero.java \
      src/main/java/com/ajedrez/modelo/ColorPieza.java \
      src/main/java/com/ajedrez/modelo/Pieza.java \
      src/main/java/com/ajedrez/modelo/Casilla.java \
      src/main/java/com/ajedrez/modelo/EstadoJuego.java \
      src/main/java/com/ajedrez/modelo/Tablero.java \
      src/main/java/com/ajedrez/modelo/piezas/Alfil.java \
      src/main/java/com/ajedrez/modelo/piezas/Rey.java \
      src/main/java/com/ajedrez/modelo/piezas/Caballo.java \
      src/main/java/com/ajedrez/modelo/piezas/Reina.java \
      src/main/java/com/ajedrez/modelo/piezas/Peon.java \
      src/main/java/com/ajedrez/modelo/piezas/Torre.java \
      src/main/java/com/ajedrez/vista/VistaTablero.java
java -version
./mvnw -version
./mvnw test
javadoc -quiet -d target/apidocs-manual \
        --module-path <jars-javafx> \
        --source-path src/main/java \
        -subpackages com.ajedrez
./mvnw javafx:run
./mvnw verify
```

Resultados principales:

| Comando | Resultado |
|---|---|
| `find src/main/java -name '*.java' -print` | Identifica 21 archivos Java. |
| `wc -l ...` | Calcula 1145 lineas Java en total. |
| `java -version` | OpenJDK 25.0.2. |
| `./mvnw -version` | Apache Maven 3.8.5 ejecutandose sobre Java 25.0.2. |
| `./mvnw test` | `BUILD SUCCESS`; no hay tests definidos. |
| `javadoc ... -subpackages com.ajedrez` | Genera documentacion JavaDoc manual en `target/apidocs-manual` sin errores. |
| `./mvnw javafx:run` | Lanza la ventana JavaFX del tablero. El proceso permanece activo mientras la ventana esta abierta. |
| `./mvnw verify` | `BUILD SUCCESS`; genera JAR e informe PMD. |

## 6. Resultados obtenidos con PMD

Ficheros generados:

| Fichero | Contenido |
|---|---|
| `target/pmd.xml` | Informe XML de PMD. |
| `target/reports/pmd.html` | Informe HTML navegable. |
| `target/pmd/rulesets/001-maven-pmd-plugin-default.xml` | Reglas aplicadas por el plugin. |

El informe `target/pmd.xml` indica:

```xml
<pmd ... version="7.7.0" timestamp="2026-05-09T14:07:43.529">
</pmd>
```

Interpretacion: no se han detectado violaciones de reglas PMD en el codigo analizado con el conjunto de reglas por defecto.

Durante la ejecucion de `./mvnw verify`, PMD mostro trazas internas con el mensaje:

```text
Unsupported class file major version 69
```

Estas trazas estan relacionadas con la compatibilidad de PMD/ASM al ejecutarse sobre OpenJDK 25.0.2, mientras que el proyecto esta configurado para compilar con Java 21. Aun asi, Maven finalizo con `BUILD SUCCESS` y el informe PMD se genero sin violaciones. Esta incidencia debe documentarse como una limitacion del entorno de analisis, no como un defecto del codigo fuente evaluado.

## 7. Listado e interpretacion de errores detectados

PMD no ha detectado errores o violaciones de reglas en el informe generado. Por tanto, no existe un listado de errores de codigo que clasificar por tipo.

Aunque no haya violaciones, el resultado permite extraer estas conclusiones:

| Resultado | Interpretacion | Relacion ISO/IEC 25000 |
|---|---|---|
| 0 violaciones de `UnusedLocalVariable`, `UnusedPrivateField`, `UnusedPrivateMethod` | No se ha detectado codigo muerto privado ni variables locales sin uso dentro del alcance de las reglas aplicadas. | Mantenibilidad: facilita comprension, analisis y modificacion. |
| 0 violaciones de reglas `errorprone` | No se han encontrado patrones comunes de error como comprobaciones nulas rotas, `finally` con `return` o bloques `catch` vacios. | Fiabilidad: reduce probabilidad de fallos por patrones conocidos. |
| 0 violaciones de reglas `codestyle` | No se han detectado imports innecesarios, retornos innecesarios o estructuras de control vacias. | Mantenibilidad: mejora legibilidad y reduce ruido. |
| 0 violaciones de reglas `performance` aplicadas | No se han encontrado los patrones de bajo rendimiento cubiertos por las reglas usadas. | Eficiencia de desempeno: ausencia de problemas concretos en las reglas revisadas. |

Limitacion importante: un resultado de 0 violaciones no significa que el software este libre de defectos. PMD solo comprueba los patrones definidos por sus reglas. Por ejemplo, las reglas de movimiento de ajedrez deben validarse tambien con pruebas unitarias, porque PMD no puede garantizar por si solo que una torre, un peon o un rey cumplan correctamente todas las reglas del juego.

## 8. Relacion con la calidad del software segun ISO/IEC 25000

La familia ISO/IEC 25000 propone evaluar la calidad del producto software mediante caracteristicas como adecuacion funcional, eficiencia de desempeno, compatibilidad, usabilidad, fiabilidad, seguridad, mantenibilidad y portabilidad.

En esta practica, PMD se relaciona especialmente con:

| Caracteristica | Relacion con PMD | Aplicacion al proyecto |
|---|---|---|
| Mantenibilidad | Detecta codigo muerto, estructuras innecesarias, imports sobrantes y complejidad accidental. | Ayuda a que las clases de piezas sean mas faciles de revisar, modificar y extender. |
| Fiabilidad | Identifica patrones propensos a fallos, como comprobaciones nulas incorrectas o bloques `catch` vacios. | Es relevante para evitar movimientos invalidos, estados incoherentes y errores en la logica del tablero. |
| Eficiencia de desempeno | Incluye reglas que detectan ciertos patrones ineficientes. | En este proyecto el impacto actual es bajo, pero sera mas relevante si se anade IA, busqueda de movimientos o validacion masiva de jugadas. |
| Seguridad | Algunas reglas evitan malas practicas como IPs hardcoded o patrones peligrosos. | Actualmente el proyecto no maneja red ni credenciales, pero mantener estas reglas previene problemas si se amplian funcionalidades. |

## 9. Conclusiones

PMD se ha integrado correctamente en el ciclo Maven del proyecto y ha generado informes en XML y HTML. El analisis del 09/05/2026 sobre 21 archivos Java y 1145 lineas de codigo no detecta violaciones con el conjunto de reglas por defecto del Maven PMD Plugin aplicado al codigo no excluido.

La ausencia de violaciones es positiva desde el punto de vista de mantenibilidad y fiabilidad basica, pero no sustituye a pruebas unitarias. La siguiente mejora recomendable para el proyecto es anadir tests JUnit para comprobar las reglas de movimiento de cada pieza y complementar PMD con reglas de diseno o complejidad mas exigentes cuando el codigo crezca.

## 10. Anexo: evidencias de salida

Salida relevante de `./mvnw test`:

```text
BUILD SUCCESS
No tests to run.
```

Salida relevante de `./mvnw verify`:

```text
BUILD SUCCESS
PMD version: 7.7.0
```

Advertencia/limitacion observada:

```text
Unsupported class file major version 69
```

Esta limitacion se debe tener en cuenta en la defensa de la practica: el analisis se completa, pero el entorno ejecuta Maven con OpenJDK 25.0.2, una version mas nueva que la indicada como objetivo de compilacion del proyecto.
