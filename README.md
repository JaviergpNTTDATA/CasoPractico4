# NOVABANK DIGITAL SERVICES – Sistema Bancario (Microservicios)

> **NovaBank Digital Services** es un sistema bancario implementado como un **conjunto de microservicios** sobre el ecosistema **Spring Boot / Spring Cloud**.
> Permite gestionar clientes, cuentas y operaciones financieras (depósitos, retiradas y transferencias) a través de una **API REST** protegida.
> El proyecto incluye **API Gateway**, **Service Discovery (Eureka)** y **Config Server** para centralizar configuración.

---

## Funcionalidades

### Autenticación / Seguridad
- Protección de rutas a través del **API Gateway**
- Validación/autorización mediante **token (JWT)**

### Gestión de clientes (client-service)
- Crear cliente
- Buscar cliente
- Listar clientes

### Gestión de cuentas (account-service)
- Crear cuenta asociada a un cliente
- Listar cuentas de un cliente
- Ver información de una cuenta
- Consultar saldo

### Operaciones (operation-service)
- Depósitos
- Retiros
- Transferencias

### Consultas
- Ver movimientos de una cuenta
- Filtrar movimientos por rango de fechas (según endpoints disponibles)

---

## Arquitectura y módulos

El repositorio contiene varios módulos Maven (microservicios):

- **config-server**: servidor de configuración centralizada (Spring Cloud Config Server)
- **config-repo/**: repositorio de configuración (YAML) consumido por el config-server
- **eureka-server**: registro y descubrimiento de servicios (Netflix Eureka)
- **api-gateway**: puerta de entrada (Spring Cloud Gateway) + filtro de autenticación
- **auth-server**: servicio de autenticación (emite/gestiona tokens)
- **client-service**: microservicio de clientes
- **account-service**: microservicio de cuentas y movimientos
- **operation-service**: microservicio de operaciones (depósito/retirada/transferencia)

---

## Autenticación para acceder a la API

1. Desde Postman/Insomnia realiza un **POST** al endpoint de login del servicio de autenticación (ejemplo):
   - `POST /auth/login`

2. Body (JSON):
```json
{
  "username": "string",
  "password": "string"
}
```

3. Si las credenciales son correctas, recibirás un **token JWT**.  
   Ese token se envía en las llamadas posteriores en la cabecera:

```
Authorization: Bearer <TOKEN>
```

> Nota: la ruta/puerto exactos dependen de tu configuración (Gateway y/o Auth Server).  
> Revisa `api-gateway` y `auth-server` (`application.yml`) y la configuración centralizada en `config-repo/`.

---

## Documentación de la API (Swagger / OpenAPI)

Si está habilitada en cada microservicio, normalmente se puede acceder a:

- `http://localhost:<PUERTO>/swagger-ui.html`  
  o
- `http://localhost:<PUERTO>/swagger-ui/index.html`

Los puertos y rutas dependen de tu `application.yml` y de `config-repo/*.yml`.

---

## Configuración (Config Server)

- La configuración centralizada vive en: `config-repo/`
  - `config-repo/application.yml`
  - `config-repo/api-gateway.yml`
  - `config-repo/client-service.yml`
  - `config-repo/account-service.yml`
  - `config-repo/operation-service.yml`

- Cada servicio también puede tener valores por defecto en `src/main/resources/application.yml`.

---

## Base de datos

Cada microservicio gestiona su persistencia de forma independiente (según su configuración).

- Revisa los `application.yml` (del servicio y/o `config-repo/`) para ver:
  - URL de datasource
  - usuario/contraseña
  - driver
  - estrategia DDL (si aplica)

---

## Cómo compilar

Desde la raíz del proyecto:

```bash
mvn clean compile
```

---

## Cómo ejecutar (local)

En un entorno de microservicios, el orden típico es:

1. **config-server**
2. **eureka-server**
3. **auth-server**
4. **api-gateway**
5. **client-service**, **account-service**, **operation-service**

Ejemplo (en diferentes terminales):

```bash
mvn -pl config-server spring-boot:run
mvn -pl eureka-server spring-boot:run
mvn -pl auth-server spring-boot:run
mvn -pl api-gateway spring-boot:run
mvn -pl client-service spring-boot:run
mvn -pl account-service spring-boot:run
mvn -pl operation-service spring-boot:run
```

---

## Cómo ejecutar los tests

```bash
mvn test
```

---

## Tecnologías usadas

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Cloud (Config Server, Eureka, Gateway)
- Spring Security + JWT
- OpenAPI / Swagger (springdoc)
- Lombok
- Maven
- JUnit 5 + Mockito
- Git

---

## Repositorio

https://github.com/JaviergpNTTDATA/CasoPractico4
