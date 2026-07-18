import React, { useState, useEffect } from 'react';
import { carroService } from '../services/api';

export default function Carros() {
  const [carros, setCarros] = useState([]);
  const [form, setForm] = useState({ marca: '', modelo: '', ano: '', preco: '', cor: '' });
  const [erro, setErro] = useState('');
  const [carroEncontrado, setCarroEncontrado] = useState(null);
  const [buscaId, setBuscaId] = useState('');
  const [erroBusca, setErroBusca] = useState('');
  const [filtroMarca, setFiltroMarca] = useState('');
  const [filtroPrecoMin, setFiltroPrecoMin] = useState('');
  const [filtroPrecoMax, setFiltroPrecoMax] = useState('');
  const [filtroAno, setFiltroAno] = useState('');
  const [estatisticas, setEstatisticas] = useState(null);
  const [erroFiltro, setErroFiltro] = useState('');

  useEffect(() => {
    carregar();
    carregarEstatisticas();
  }, []);

  const carregar = async () => {
    const res = await carroService.listarTodos();
    setCarros(res.data);
  };

  const carregarEstatisticas = async () => {
    try {
      const res = await carroService.estatisticas();
      setEstatisticas(res.data);
    } catch (err) {
      console.error('Erro ao carregar estatísticas');
    }
  };

  const salvar = async (e) => {
    e.preventDefault();
    try {
      await carroService.cadastrar(form);
      setForm({ marca: '', modelo: '', ano: '', preco: '', cor: '' });
      setErro('');
      carregar();
      carregarEstatisticas();
    } catch (err) {
      setErro('Erro ao cadastrar carro.');
    }
  };

  const deletar = async (id) => {
    if (!window.confirm('Excluir este carro?')) return;
    await carroService.deletar(id);
    carregar();
    carregarEstatisticas();
  };

  const buscarPorId = async () => {
    if (!buscaId) { setErroBusca('Digite um ID!'); return; }
    try {
      const res = await carroService.buscarPorId(buscaId);
      setCarroEncontrado(res.data);
      setErroBusca('');
    } catch (err) {
      setCarroEncontrado(null);
      setErroBusca(`Carro com ID ${buscaId} não encontrado.`);
    }
  };

  const filtrarPorMarca = async () => {
    if (!filtroMarca.trim()) { setErroFiltro('Digite uma marca!'); return; }
    try {
      const res = await carroService.listarPorMarca(filtroMarca);
      setCarros(res.data);
      setErroFiltro('');
    } catch (err) {
      setErroFiltro('Erro ao filtrar por marca.');
    }
  };

  const filtrarPorPreco = async () => {
    if (!filtroPrecoMin || !filtroPrecoMax) { setErroFiltro('Preencha o preço mínimo e máximo!'); return; }
    try {
      const res = await carroService.listarPorPreco(filtroPrecoMin, filtroPrecoMax);
      setCarros(res.data);
      setErroFiltro('');
    } catch (err) {
      setErroFiltro('Erro ao filtrar por preço.');
    }
  };

  const filtrarPorAno = async () => {
    if (!filtroAno) { setErroFiltro('Digite um ano!'); return; }
    try {
      const res = await carroService.listarPorAno(filtroAno);
      setCarros(res.data);
      setErroFiltro('');
    } catch (err) {
      setErroFiltro('Erro ao filtrar por ano.');
    }
  };

  const limparFiltros = () => {
    setFiltroMarca('');
    setFiltroPrecoMin('');
    setFiltroPrecoMax('');
    setFiltroAno('');
    setErroFiltro('');
    carregar();
  };

  const statusBadge = (status) => {
    const classe = { DISPONIVEL: 'verde', VENDIDO: 'vermelho', RESERVADO: 'amarelo' };
    return <span className={`badge ${classe[status]}`}>{status}</span>;
  };

  return (
    <div>
      <h2>🚘 Estoque de Carros</h2>

      {estatisticas && (
        <div className="cards-grid">
          <div className="card-stat">
            <div className="card-stat-valor escuro">{estatisticas.total}</div>
            <div className="card-stat-label">Total</div>
          </div>
          <div className="card-stat">
            <div className="card-stat-valor verde">{estatisticas.disponiveis}</div>
            <div className="card-stat-label">Disponíveis</div>
          </div>
          <div className="card-stat">
            <div className="card-stat-valor vermelho">{estatisticas.vendidos}</div>
            <div className="card-stat-label">Vendidos</div>
          </div>
          <div className="card-stat">
            <div className="card-stat-valor amarelo">{estatisticas.reservados}</div>
            <div className="card-stat-label">Reservados</div>
          </div>
        </div>
      )}

      <form onSubmit={salvar} className="form-card">
        <h3>Cadastrar Carro</h3>
        <div className="form-grid">
          <input placeholder="Marca *" value={form.marca}
            onChange={e => setForm({ ...form, marca: e.target.value })} />
          <input placeholder="Modelo *" value={form.modelo}
            onChange={e => setForm({ ...form, modelo: e.target.value })} />
          <input placeholder="Ano *" type="number" value={form.ano}
            onChange={e => setForm({ ...form, ano: e.target.value })} />
          <input placeholder="Preço *" type="number" value={form.preco}
            onChange={e => setForm({ ...form, preco: e.target.value })} />
          <input placeholder="Cor" value={form.cor}
            onChange={e => setForm({ ...form, cor: e.target.value })} />
        </div>
        {erro && <p className="erro">{erro}</p>}
        <button type="submit">+ Cadastrar</button>
      </form>

      <div className="form-card">
        <h3>🔍 Buscar Carro por ID</h3>
        <div className="busca-id-row">
          <input className="filtro-input lg" type="number"
            placeholder="Digite o ID do carro"
            value={buscaId} onChange={e => setBuscaId(e.target.value)} />
          <button className="btn-filtro azul" onClick={buscarPorId}>Buscar</button>
          {carroEncontrado && (
            <button className="btn-filtro cinza"
              onClick={() => { setCarroEncontrado(null); setBuscaId(''); }}>
              Limpar
            </button>
          )}
        </div>
        {erroBusca && <p className="erro">{erroBusca}</p>}
        {carroEncontrado && (
          <div className="busca-resultado">
            <p>Carro encontrado</p>
            <table>
              <tbody>
                {[
                  ['ID', carroEncontrado.id],
                  ['Marca', carroEncontrado.marca],
                  ['Modelo', carroEncontrado.modelo],
                  ['Ano', carroEncontrado.ano],
                  ['Cor', carroEncontrado.cor || '—'],
                  ['Preço', `R$ ${carroEncontrado.preco?.toLocaleString('pt-BR')}`],
                  ['Status', carroEncontrado.status],
                ].map(([label, valor]) => (
                  <tr key={label}>
                    <td>{label}</td>
                    <td>{valor}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      <div className="form-card">
        <h3>🔎 Filtrar Estoque</h3>
        <div className="filtros-row">
          <input className="filtro-input md" placeholder="Filtrar por marca"
            value={filtroMarca} onChange={e => setFiltroMarca(e.target.value)} />
          <button className="btn-filtro roxo" onClick={filtrarPorMarca}>Por Marca</button>

          <input className="filtro-input md" type="number" placeholder="Preço mín (R$)"
            value={filtroPrecoMin} onChange={e => setFiltroPrecoMin(e.target.value)} />
          <input className="filtro-input md" type="number" placeholder="Preço máx (R$)"
            value={filtroPrecoMax} onChange={e => setFiltroPrecoMax(e.target.value)} />
          <button className="btn-filtro verde" onClick={filtrarPorPreco}>Por Preço</button>

          <input className="filtro-input sm" type="number" placeholder="Ano mínimo"
            value={filtroAno} onChange={e => setFiltroAno(e.target.value)} />
          <button className="btn-filtro laranja" onClick={filtrarPorAno}>Por Ano</button>

          <button className="btn-filtro cinza" onClick={limparFiltros}>Limpar Filtros</button>
        </div>
        {erroFiltro && <p className="erro">{erroFiltro}</p>}
      </div>

      <table className="tabela">
        <thead>
          <tr><th>ID</th><th>Marca</th><th>Modelo</th><th>Ano</th>
              <th>Cor</th><th>Preço</th><th>Status</th><th>Ações</th></tr>
        </thead>
        <tbody>
          {carros.length === 0
            ? <tr><td colSpan="8" className="centro" style={{ padding: '20px', color: '#888' }}>
                Nenhum carro encontrado.
              </td></tr>
            : carros.map(c => (
              <tr key={c.id}>
                <td>{c.id}</td>
                <td>{c.marca}</td>
                <td>{c.modelo}</td>
                <td>{c.ano}</td>
                <td>{c.cor || '—'}</td>
                <td>R$ {c.preco?.toLocaleString('pt-BR')}</td>
                <td>{statusBadge(c.status)}</td>
                <td>
                  {c.status === 'DISPONIVEL' && (
                    <button className="btn-danger" onClick={() => deletar(c.id)}>Excluir</button>
                  )}
                </td>
              </tr>
            ))
          }
        </tbody>
      </table>
    </div>
  );
}