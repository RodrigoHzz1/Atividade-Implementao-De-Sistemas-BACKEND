-- =============================================================================
-- Script OPCIONAL de manutenção de dados.
--
-- Contexto: chamados criados antes do campo `nivelAtual` existir/ter valor
-- padrão (NivelSuporte.N1) podem ter a coluna nivel_atual (Tab_Chamado) NULL
-- no banco. O código Java já foi corrigido para nunca gravar nivel_suporte
-- nulo em Tab_Atendimento (usa fallback N1), então este script NÃO é
-- obrigatório para o sistema voltar a funcionar. Ele serve apenas para
-- normalizar os dados antigos, caso deseje manter tudo consistente no banco.
--
-- Execute manualmente no MySQL (schema att_helpD) se desejar.
-- =============================================================================

-- 1) Preenche nivel_atual nulo em chamados antigos com o padrão N1
UPDATE Tab_Chamado
SET nivel_atual = 'N1'
WHERE nivel_atual IS NULL;

-- 2) (Opcional) Caso queira que a coluna nivel_suporte de Tab_Atendimento
--    aceite NULL no banco, refletindo o nullable = true já presente na
--    entidade AtendimentoModel (o Hibernate com ddl-auto=update não altera
--    constraints de colunas já existentes automaticamente):
-- ALTER TABLE Tab_Atendimento MODIFY COLUMN nivel_suporte VARCHAR(20) NULL;
