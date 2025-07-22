# 🔥  java-backend-quarkus

Application Java with Quarkus 100% Serverless

## 📄 Descripción

Este proyecto implementa una solución **serverless**. Esta solución no utiliza servidores EC2, tampoco contenedores Kuberrnetes. 
Es totalemente administrado por AWS el cual provee elasticidad, escalabilidad y alta disponibilidad.
Este es un proyecto backend java, que puedes utilizar con tus proyectos de frontend.

Utilizamos **Amazon API Gateway** que se comunica muy bien con  **AWS CloudFormation**. Vemos que ha creado el API

También tenemos la base de datos 100% serverless **Amazon DynamoDB**
Esta solución
---

## 🏗️ Arquitectura

```plaintext
   [Java]
                 │
                 ▼
    🔥 Quarkus ya tiene Programación Reactiva
                 │
    (AWS Handler ya tiene Programación Reactiva)
                 │
                 ▼
       📂 Amazon Handler (100% serverless, ahorro en costo de servidores)
                 │
                 ▼
      (Se ejecutan los métodos de Backend)
```

---

## 🛠️ Servicios AWS utilizados

- **Amazon API Gateway**
  - Permite controlar CORS .
  - Permite HTTP RESTful.
  - Proporciona el API para cliente frontend.
    ![img_1.png](img_1.png)

- **AWS CloudFormation**
  - Proporciona IaC.
  - Crea el Stack en los ambientes Dev y Prod.
    ![img_2.png](img_2.png)

- **Amazon DynamoDB**
  - Base de Datos 100% serverless.
  - Es no relacional.
    ![img_3.png](img_3.png)

- **AWS SAM (Serverless Application Model)**
  - Despliegue de la infraestructura como código (`template.yaml`).

---

## 🚀 Despliegue del proyecto

### 1️⃣ GitHub Actions
- Diríjase al link Actions, allí podrá visualizar el despliegue DevOps.
  ![img_4.png](img_4.png)

### 2️⃣ Clona el repositorio y navega al proyecto:
```bash
git clone https://github.com/tu-usuario/java-backend-quarkus.git
cd java-backend-quarkus
```

### 3️⃣ Despliegue:
```bash
sam build
sam deploy --guided
```

Durante el despliegue:
- Proporciona el nombre del stack.
- Define la región AWS.
- Acepta permisos de tipo `CAPABILITY_IAM`.

---

## 📊 Consulta de datos

Una vez que Firehose empiece a recibir datos:
- Los archivos **Parquet** estarán disponibles en el bucket S3.
- Puedes consultar los datos usando:
  - **Amazon Athena**
  - **Amazon Redshift Spectrum**
  - Herramientas externas compatibles con Parquet.

---

## 📈 Beneficios del proyecto

- ✅ Completamente **serverless**.
- ✅ Alta disponibilidad, funciona 24x7x365 sin caídas.
- ✅ Totalmente elática, si se conectan muchos usuarios, soporta la concurrencia sin límite.
- ✅ Arquitectura automatizada, simple y escalable. Para integrar más recursos y servicios.
- ✅ Costo controlado muy bajo 1$ al mes, sin servidores ni procesamiento batch complejo.

---

## 📋 Consideraciones adicionales

- Este proyecto AWS puede ser montado para su empresa.
- El `template.yaml` crea automáticamente:
  - El cloudformation.
  - La base de datos.
  - El AWS api gateway.

- El proyecto es ideal para ahorrar costos y obtener alta rentabilidad.

---


## 📜 Licencia

Este proyecto es de código abierto. Puedes adaptarlo y reutilizarlo bajo los términos de tu organización.

---

## 🧑 Contacto

**Paul Rivera**
AWS Certified Solutions Architect - Associate
Java Developer