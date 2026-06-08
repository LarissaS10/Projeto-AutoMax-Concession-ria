import React, { useState, useEffect } from 'react';
import { carroService } from '../services/api';

export default function Carros() {
  const [carros, setCarros] = useState([]);
  const [form, setForm] = useState({ marca: '', modelo: '', ano: '', preco: '', cor: '' });
  const [erro, setErro] = useState('');
  const [carroEncontrado, setCarroEncontrado] = useState(null);
  const [buscaId, setBuscaId] = useState('');
  const [erroBusca, setErroBusca] = useState('');

  useEffect(() => { carregar(); }, []);

  const carregar = async () => {
    const res = await carroService.listarTodos();
    setCarros(res.data);
  };

  const salvar = async (e) => {
    e.preventDefault();
    try {
      await carroService.cadastrar(form);
      setForm({ marca: '', modelo: '', ano: '', preco: '', cor: '' });
      setErro('');
      carregar();
    } catch (err) {
      setErro('Erro ao cadastrar carro.');
    }
  };

  const deletar = async (id) => {
    if (!window.confirm('Excluir este carro?')) return;
    await carroService.deletar(id);
    carregar();
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

  const statusBadge = (status) => {
    const cores = { DISPONIVEL: '#2ecc71', VENDIDO: '#e74c3c', RESERVADO: '#f39c12' };
    return (
      <span style={{
        background: cores[status], color: 'white',
        padding: '2px 10px', borderRadius: '12px', fontSize: '12px'
      }}>{status}</span>
    );
  };

  return (
    <div>
      <h2>🚘 Estoque de Carros</h2>

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

      {/* Busca por ID */}
      <div style={{ margin: '20px 0' }}>
        <h3 style={{ marginBottom: '10px' }}>🔍 Buscar Carro por ID</h3>

        <div style={{ display: 'flex', gap: '10px', marginBottom: '10px' }}>
          <input
            type="number"
            placeholder="Digite o ID do carro"
            value={buscaId}
            onChange={e => setBuscaId(e.target.value)}
            style={{ padding: '10px 14px', border: '1px solid #ddd',
                     borderRadius: '8px', fontSize: '14px', width: '220px' }}
          />
          <button
            onClick={buscarPorId}
            style={{ padding: '10px 20px', background: '#3498db', color: 'white',
                     border: 'none', borderRadius: '8px', cursor: 'pointer', fontSize: '14px' }}>
            Buscar
          </button>
          {carroEncontrado && (
            <button
              onClick={() => { setCarroEncontrado(null); setBuscaId(''); }}
              style={{ padding: '10px 20px', background: '#95a5a6', color: 'white',
                       border: 'none', borderRadius: '8px', cursor: 'pointer', fontSize: '14px' }}>
              Limpar
            </button>
          )}
        </div>

        {erroBusca && (
          <p style={{ color: '#e74c3c', fontSize: '13px', marginBottom: '8px' }}>
            {erroBusca}
          </p>
        )}

        {carroEncontrado && (
          <div style={{ background: '#f0f9ff', border: '1px solid #3498db',
                        borderRadius: '10px', padding: '16px', maxWidth: '400px' }}>
            <p style={{ fontWeight: 'bold', marginBottom: '8px', color: '#2c3e50' }}>
              Carro encontrado
            </p>
            <table style={{ fontSize: '14px', width: '100%' }}>
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
                    <td style={{ color: '#666', paddingBottom: '4px',
                                 width: '80px', fontWeight: '500' }}>{label}</td>
                    <td style={{ paddingBottom: '4px' }}>{valor}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      <table className="tabela">
        <thead>
          <tr><th>ID</th><th>Marca</th><th>Modelo</th><th>Ano</th>
              <th>Cor</th><th>Preço</th><th>Status</th><th>Ações</th></tr>
        </thead>
        <tbody>
          {carros.map(c => (
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
          ))}
        </tbody>
      </table>
    </div>
  );
}