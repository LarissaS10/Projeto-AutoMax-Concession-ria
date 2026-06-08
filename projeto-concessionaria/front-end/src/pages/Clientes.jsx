import React, { useState, useEffect } from 'react';
import { clienteService } from '../services/api';

export default function Clientes() {
  const [clientes, setClientes] = useState([]);
  const [form, setForm] = useState({ nome: '', cpf: '', email: '', telefone: '' });
  const [erro, setErro] = useState('');

  useEffect(() => { carregar(); }, []);

  const carregar = async () => {
    const res = await clienteService.listarTodos();
    setClientes(res.data);
  };

  const salvar = async (e) => {
    e.preventDefault();
    try {
      await clienteService.cadastrar(form);
      setForm({ nome: '', cpf: '', email: '', telefone: '' });
      setErro('');
      carregar();
    } catch (err) {
      setErro(err.response?.data?.message || 'Erro ao cadastrar cliente.');
    }
  };

  return (
    <div>
      <h2>👤 Clientes</h2>

      <form onSubmit={salvar} className="form-card">
        <h3>Cadastrar Cliente</h3>
        <div className="form-grid">
          <input placeholder="Nome *" value={form.nome}
            onChange={e => setForm({ ...form, nome: e.target.value })} />
          <input placeholder="CPF *" value={form.cpf}
            onChange={e => setForm({ ...form, cpf: e.target.value })} />
          <input placeholder="E-mail" value={form.email}
            onChange={e => setForm({ ...form, email: e.target.value })} />
          <input placeholder="Telefone" value={form.telefone}
            onChange={e => setForm({ ...form, telefone: e.target.value })} />
        </div>
        {erro && <p className="erro">{erro}</p>}
        <button type="submit">+ Cadastrar</button>
      </form>

      <table className="tabela">
        <thead>
          <tr><th>ID</th><th>Nome</th><th>CPF</th><th>E-mail</th><th>Telefone</th></tr>
        </thead>
        <tbody>
          {clientes.map(c => (
            <tr key={c.id}>
              <td>{c.id}</td><td>{c.nome}</td><td>{c.cpf}</td>
              <td>{c.email || '—'}</td><td>{c.telefone || '—'}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}