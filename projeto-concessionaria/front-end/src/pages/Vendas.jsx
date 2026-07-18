import React, { useState, useEffect } from 'react';
import { vendaService, carroService, clienteService } from '../services/api';

export default function Vendas() {
  const [vendas, setVendas] = useState([]);
  const [carros, setCarros] = useState([]);
  const [clientes, setClientes] = useState([]);
  const [form, setForm] = useState({ clienteId: '', carroId: '', valorFinal: '' });
  const [erro, setErro] = useState('');
  const [sucesso, setSucesso] = useState('');
  const [receita, setReceita] = useState(null);
  const [filtroValorMin, setFiltroValorMin] = useState('');
  const [erroFiltro, setErroFiltro] = useState('');
  const [vendaMes, setVendaMes] = useState(null);

  useEffect(() => {
    carregar();
    carregarReceita();
    carregarVendasMes();
  }, []);

  const carregar = async () => {
    const [v, ca, cl] = await Promise.all([
      vendaService.listarTodas(),
      carroService.listarDisponiveis(),
      clienteService.listarTodos(),
    ]);
    setVendas(v.data);
    setCarros(ca.data);
    setClientes(cl.data);
  };

  const carregarReceita = async () => {
    try {
      const res = await vendaService.receitaTotal();
      setReceita(res.data);
    } catch (err) {
      console.error('Erro ao carregar receita');
    }
  };

  const carregarVendasMes = async () => {
    try {
      const res = await vendaService.vendasMesAtual();
      setVendaMes(res.data.length);
    } catch (err) {
      console.error('Erro ao carregar vendas do mês');
    }
  };

  const realizar = async (e) => {
    e.preventDefault();
    try {
      await vendaService.realizar(form);
      setForm({ clienteId: '', carroId: '', valorFinal: '' });
      setSucesso('Venda realizada com sucesso! 🎉');
      setErro('');
      carregar();
      carregarReceita();
      carregarVendasMes();
    } catch (err) {
      setErro(err.response?.data?.message || 'Erro ao realizar venda.');
      setSucesso('');
    }
  };

  const filtrarPorValor = async () => {
    if (!filtroValorMin) { setErroFiltro('Digite um valor mínimo!'); return; }
    try {
      const res = await vendaService.listarPorValorMinimo(filtroValorMin);
      setVendas(res.data);
      setErroFiltro('');
    } catch (err) {
      setErroFiltro('Erro ao filtrar por valor.');
    }
  };

  const limparFiltros = () => {
    setFiltroValorMin('');
    setErroFiltro('');
    carregar();
  };

  return (
    <div>
      <h2>💰 Vendas</h2>

      <div className="cards-grid-3">
        <div className="card-stat">
          <div className="card-stat-valor escuro">{vendas.length}</div>
          <div className="card-stat-label">Total de vendas</div>
        </div>
        <div className="card-stat">
          <div className="card-stat-valor azul">{vendaMes ?? '—'}</div>
          <div className="card-stat-label">Vendas este mês</div>
        </div>
        <div className="card-stat">
          <div className="card-stat-valor verde-escuro">
            {receita != null ? `R$ ${Number(receita).toLocaleString('pt-BR')}` : '—'}
          </div>
          <div className="card-stat-label">Receita total</div>
        </div>
      </div>

      {/* Formulário */}
      <form onSubmit={realizar} className="form-card">
        <h3>Registrar Venda</h3>
        <div className="form-grid">
          <select value={form.clienteId}
            onChange={e => setForm({ ...form, clienteId: e.target.value })}>
            <option value="">Selecione o cliente *</option>
            {clientes.map(c => <option key={c.id} value={c.id}>{c.nome} — {c.cpf}</option>)}
          </select>
          <select value={form.carroId}
            onChange={e => {
              const carro = carros.find(c => c.id === Number(e.target.value));
              setForm({ ...form, carroId: e.target.value, valorFinal: carro?.preco || '' });
            }}>
            <option value="">Selecione o carro *</option>
            {carros.map(c => (
              <option key={c.id} value={c.id}>
                {c.marca} {c.modelo} ({c.ano}) — R$ {c.preco?.toLocaleString('pt-BR')}
              </option>
            ))}
          </select>
          <input placeholder="Valor Final (R$)" type="number" value={form.valorFinal}
            onChange={e => setForm({ ...form, valorFinal: e.target.value })} />
        </div>
        {erro && <p className="erro">{erro}</p>}
        {sucesso && <p className="sucesso">{sucesso}</p>}
        <button type="submit">✅ Confirmar Venda</button>
      </form>

      <div className="form-card">
        <h3>🔎 Filtrar Vendas</h3>
        <div className="filtros-row">
          <input className="filtro-input md" type="number"
            placeholder="Valor mínimo (R$)"
            value={filtroValorMin}
            onChange={e => setFiltroValorMin(e.target.value)} />
          <button className="btn-filtro verde" onClick={filtrarPorValor}>Por Valor Mínimo</button>
          <button className="btn-filtro cinza" onClick={limparFiltros}>Limpar Filtros</button>
        </div>
        {erroFiltro && <p className="erro">{erroFiltro}</p>}
      </div>

      <table className="tabela">
        <thead>
          <tr><th>ID</th><th>Cliente</th><th>Carro</th><th>Valor Final</th><th>Data</th></tr>
        </thead>
        <tbody>
          {vendas.length === 0
            ? <tr><td colSpan="5" className="centro" style={{ padding: '20px', color: '#888' }}>
                Nenhuma venda encontrada.
              </td></tr>
            : vendas.map(v => (
              <tr key={v.id}>
                <td>{v.id}</td>
                <td>{v.cliente?.nome}</td>
                <td>{v.carro?.marca} {v.carro?.modelo}</td>
                <td>R$ {v.valorFinal?.toLocaleString('pt-BR')}</td>
                <td>{new Date(v.dataVenda).toLocaleString('pt-BR')}</td>
              </tr>
            ))
          }
        </tbody>
      </table>
    </div>
  );
}