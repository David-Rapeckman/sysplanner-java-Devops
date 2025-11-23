-- scripts/script-bd.sql

IF OBJECT_ID('dbo.lembretes', 'U') IS NOT NULL
    DROP TABLE dbo.lembretes;

IF OBJECT_ID('dbo.usuarios', 'U') IS NOT NULL
    DROP TABLE dbo.usuarios;
GO

CREATE TABLE usuarios (
    id           BIGINT IDENTITY(1,1) PRIMARY KEY,
    nome         NVARCHAR(150) NOT NULL,
    email        NVARCHAR(150) NOT NULL UNIQUE,
    senha        NVARCHAR(255) NOT NULL,
    cpf          NVARCHAR(11)  NULL,

    rua          NVARCHAR(150) NOT NULL,
    numero       NVARCHAR(10)  NOT NULL,
    complemento  NVARCHAR(50)  NULL,
    bairro       NVARCHAR(100) NOT NULL,
    cidade       NVARCHAR(100) NOT NULL,
    estado       NVARCHAR(2)   NOT NULL,
    cep          NVARCHAR(8)   NOT NULL,

    role         NVARCHAR(20)  NOT NULL
);
GO

CREATE TABLE lembretes (
    id          BIGINT IDENTITY(1,1) PRIMARY KEY,
    titulo      NVARCHAR(150)   NOT NULL,
    descricao   NVARCHAR(2000)  NOT NULL,
    data        DATETIME2       NOT NULL,
    prioridade  NVARCHAR(20)    NOT NULL,
    categoria   NVARCHAR(20)    NOT NULL,
    concluido   NVARCHAR(1)     NOT NULL DEFAULT 'N',

    usuario_id  BIGINT          NOT NULL,
    CONSTRAINT fk_lembrete_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
);
GO
