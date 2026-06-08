import React, { useState, useEffect } from 'react';
import { vendaService, carroService, clienteService } from '../services/api';

export default function Vendas() {
  const [vendas, setVendas] = useState([]);
  const [carros, setCarros] = useState([]);
  const [clientes, setClientes] = useState([]);
  const [form, setForm] = useState({ clienteId: '', carroId: '', valorFinal: '' });
  const [erro, setErro] = useState('');
  const [sucesso, setSucesso] = useState('');

  useEffect(() => { carregar(); }, []);

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

  const realizar = async (e) => {
    e.preventDefault();
    try {
      await vendaService.realizar(form);
      setForm({ clienteId: '', carroId: '', valorFinal: '' });
      setSucesso('Venda realizada com sucesso! 🎉');
      setErro('');
      carregar();
    } catch (err) {
      setErro(err.response?.data?.message || 'Erro ao realizar venda.');
      setSucesso('');
    }
  };

  return (
    <div>
      <h2>💰 Vendas</h2>

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

      <table className="tabela">
        <thead>
          <tr><th>ID</th><th>Cliente</th><th>Carro</th><th>Valor Final</th><th>Data</th></tr>
        </thead>
        <tbody>
          {vendas.map(v => (
            <tr key={v.id}>
              <td>{v.id}</td>
              <td>{v.cliente?.nome}</td>
              <td>{v.carro?.marca} {v.carro?.modelo}</td>
              <td>R$ {v.valorFinal?.toLocaleString('pt-BR')}</td>
              <td>{new Date(v.dataVenda).toLocaleString('pt-BR')}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}