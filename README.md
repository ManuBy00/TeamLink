# 💬 TeamLink: Sistema de Comunicación Empresarial OFFLINE

TeamLink es una aplicación de escritorio desarrollada en JavaFX que simula un sistema de comunicación interna para empresas. Su característica distintiva es que toda la persistencia de datos (usuarios, chats y mensajes) se gestiona **localmente** utilizando archivos **XML** y la tecnología JAXB, sin depender de servicios o bases de datos externas.

## ✨ Características Principales

| Funcionalidad | Descripción |
| :--- | :--- |
| **Arquitectura de Usuarios** | Soporte para dos roles (`Usuario` base): **Empresa** (administrador, creador de grupos) y **Empleado** (usuario estándar). |
| **Mensajería Persistente** | Gestión de chats privados y grupales donde todos los mensajes se almacenan en el archivo `Chats.XML`. |
| **Archivos Adjuntos** | Permite adjuntar archivos (imágenes, PDF), guardándolos en la carpeta local `MEDIA_DIR/` y almacenando sus metadatos (`Adjunto`) dentro del `Mensaje` en el XML. |
| **Análisis de Datos** | Generación de informes estadísticos avanzados de las conversaciones, utilizando **Java Streams** para calcular conteo por usuario y las palabras más comunes. |
| **Exportación Segura** | Opción para empaquetar conversaciones y todos sus adjuntos binarios relacionados en un único archivo **ZIP**. |

***

## ⚙️ Arquitectura y Persistencia

### 1. Gestión de Datos (Singleton)

El proyecto utiliza el patrón Singleton para el acceso centralizado y seguro a los datos:

* **`UsuariosManager`:** Lee y escribe `Usuarios.XML`.
* **`ChatsManager`:** Lee y escribe `Chats.XML`.

### 2. Modelo de Referencia y JAXB

Para evitar la redundancia y facilitar la deserialización con JAXB, las entidades usan referencias (`int` o `String`) en lugar de objetos anidados:

* **`Usuario`** (clase base) guarda `HashSet<Integer> chatsID` (referencia al ID del chat).
* **`Empresa`** guarda `HashSet<String> empleados` (referencia al email del empleado).

### 3. Persistencia de ID Secuencial

Para evitar la duplicación de ID en los chats (`Chat.java`), el `ChatsManager` mantiene un campo de instancia `nextChatId` que se serializa junto con los chats. Esto garantiza que el contador se reanude correctamente después de reiniciar la aplicación.

***

## ▶️ Tecnologías y Requisitos

| Tecnología | Propósito |
| :--- | :--- |
| **Lenguaje** | Java (JDK 21+) |
| **Interfaz Gráfica** | **JavaFX** |
| **Serialización** | **JAXB** (Jakarta XML Binding) |
| **Análisis de Datos** | **Java Streams** |
| **Seguridad** | **jBCrypt** (para hasheo de contraseñas) |
| **I/O Avanzada** | `java.util.zip.ZipOutputStream` |
