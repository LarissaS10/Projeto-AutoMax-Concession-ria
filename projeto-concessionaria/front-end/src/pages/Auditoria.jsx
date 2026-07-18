import React, { useState, useEffect } from 'react';
import { auditoriaService } from '../services/api';

export default function Auditoria() {
  const [historico, setHistorico] = useState([]);
  const [filtroEntidade, setFiltroEntidade] = useState('');
  const [carregando, setCarregando] = useState(false);
  const [erro, setErro] = useState('');

  useEffect(() => { carregar(); }, []);

  const carregar = async () => {
    setCarregando(true);
    try {
      const res = await auditoriaService.listarTodos();
      setHistorico(res.data);
      setErro('');
    } catch (err) {
      setErro('Erro ao carregar histórico.');
    } finally {
      setCarregando(false);
    }
  };

  const filtrarPorEntidade = async (entidade) => {
    setFiltroEntidade(entidade);
    setCarregando(true);
    try {
      const res = entidade
        ? await auditoriaService.listarPorEntidade(entidade)
        : await auditoriaService.listarTodos();
      setHistorico(res.data);
      setErro('');
    } catch (err) {
      setErro('Erro ao filtrar histórico.');
    } finally {
      setCarregando(false);
    }
  };

  const operacaoBadge = (operacao) => {
    const classe = {
      CRIACAO:     'verde',
      ATUALIZACAO: 'azul',
      REMOCAO:     'vermelho',
    };
    const texto = {
      CRIACAO:     '✚ Criação',
      ATUALIZACAO: '✎ Atualização',
      REMOCAO:     '✕ Remoção',
    };
    return (
      <span className={`badge ${classe[operacao] || 'cinza'}`}>
        {texto[operacao] || operacao}
      </span>
    );
  };

  const entidadeBadge = (entidade) => {
    const classe = { Carro: 'roxo', Cliente: 'laranja', Venda: 'verde-agua' };
    return <span className={`badge ${classe[entidade] || 'cinza'}`}>{entidade}</span>;
  };

  return (
    <div>
      <h2>📋 Histórico de Auditoria</h2>

      <div className="form-card">
        <h3>Filtrar por Domínio</h3>
        <div className="filtros-row">
          {[
            { valor: '',        label: '📋 Todos'    },
            { valor: 'Carro',   label: '🚘 Carros'   },
            { valor: 'Cliente', label: '👤 Clientes' },
            { valor: 'Venda',   label: '💰 Vendas'   },
          ].map(({ valor, label }) => (
            <button
              key={valor}
              className={`btn-selecao ${filtroEntidade === valor ? 'ativo' : ''}`}
              onClick={() => filtrarPorEntidade(valor)}>
              {label}
            </button>
          ))}
          <button className="btn-filtro cinza" onClick={carregar}>🔄 Atualizar</button>
        </div>
      </div>

      {erro && <p className="erro">{erro}</p>}
      {carregando && <p style={{ color: '#888', margin: '10px 0' }}>Carregando...</p>}

      <table className="tabela">
        <thead>
          <tr>
            <th>ID</th>
            <th>Domínio</th>
            <th>ID Registro</th>
            <th>Operação</th>
            <th>Dados Anteriores</th>
            <th>Dados Novos</th>
            <th>Data/Hora</th>
          </tr>
        </thead>
        <tbody>
          {historico.length === 0 && !carregando
            ? <tr><td colSpan="7" className="centro" style={{ padding: '20px', color: '#888' }}>
                Nenhum registro de auditoria encontrado.
              </td></tr>
            : historico.map(h => (
              <tr key={h.id}>
                <td>{h.id}</td>
                <td>{entidadeBadge(h.entidade)}</td>
                <td className="centro">{h.entidadeId}</td>
                <td>{operacaoBadge(h.operacao)}</td>
                <td className="texto-pequeno">{h.dadosAnteriores || '—'}</td>
                <td className="texto-pequeno">{h.dadosNovos || '—'}</td>
                <td className="nowrap">{new Date(h.dataHora).toLocaleString('pt-BR')}</td>
              </tr>
            ))
          }
        </tbody>
      </table>
    </div>
  );
}