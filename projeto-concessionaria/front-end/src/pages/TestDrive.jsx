import React, { useState, useEffect } from 'react';
import { testDriveService, carroService } from '../services/api';

export default function TestDrive() {
  const [testDrives, setTestDrives] = useState([]);
  const [carros, setCarros] = useState([]);
  const [estatisticas, setEstatisticas] = useState(null);
  const [filtroStatus, setFiltroStatus] = useState('');
  const [erro, setErro] = useState('');
  const [sucesso, setSucesso] = useState('');
  const [carregando, setCarregando] = useState(false);
  const [form, setForm] = useState({
    carroId: '',
    carroMarca: '',
    carroModelo: '',
    clienteNome: '',
    clienteTelefone: '',
    dataAgendada: '',
    observacoes: '',
  });

  useEffect(() => {
    carregar();
    carregarEstatisticas();
    carregarCarros();
  }, []);

  const carregar = async () => {
    setCarregando(true);
    try {
      const res = await testDriveService.listarTodos();
      setTestDrives(res.data);
      setErro('');
    } catch (err) {
      setErro('Erro ao carregar test drives. O microsserviço está rodando?');
    } finally {
      setCarregando(false);
    }
  };

  const carregarEstatisticas = async () => {
    try {
      const res = await testDriveService.estatisticas();
      setEstatisticas(res.data);
    } catch (err) {
      console.error('Erro ao carregar estatísticas');
    }
  };

  const carregarCarros = async () => {
    try {
      const res = await carroService.listarDisponiveis();
      setCarros(res.data);
    } catch (err) {
      console.error('Erro ao carregar carros');
    }
  };

  const agendar = async (e) => {
    e.preventDefault();
    if (!form.carroId || !form.clienteNome || !form.clienteTelefone || !form.dataAgendada) {
      setErro('Preencha todos os campos obrigatórios!');
      return;
    }
    try {
      await testDriveService.agendar({
        ...form,
        dataAgendada: new Date(form.dataAgendada).toISOString(),
      });
      setForm({
        carroId: '', carroMarca: '', carroModelo: '',
        clienteNome: '', clienteTelefone: '',
        dataAgendada: '', observacoes: '',
      });
      setSucesso('Test Drive agendado com sucesso! 🎉');
      setErro('');
      carregar();
      carregarEstatisticas();
    } catch (err) {
      setErro('Erro ao agendar test drive.');
      setSucesso('');
    }
  };

  const realizar = async (id) => {
    try {
      await testDriveService.realizar(id);
      setSucesso('Test Drive marcado como realizado!');
      carregar();
      carregarEstatisticas();
    } catch (err) {
      setErro(err.response?.data?.message || 'Erro ao realizar test drive.');
    }
  };

  const cancelar = async (id) => {
    if (!window.confirm('Cancelar este test drive?')) return;
    try {
      await testDriveService.cancelar(id);
      setSucesso('Test Drive cancelado.');
      carregar();
      carregarEstatisticas();
    } catch (err) {
      setErro(err.response?.data?.message || 'Erro ao cancelar test drive.');
    }
  };

  const filtrarPorStatus = async (status) => {
    setFiltroStatus(status);
    setCarregando(true);
    try {
      const res = status
        ? await testDriveService.listarPorStatus(status)
        : await testDriveService.listarTodos();
      setTestDrives(res.data);
    } catch (err) {
      setErro('Erro ao filtrar.');
    } finally {
      setCarregando(false);
    }
  };

  const selecionarCarro = (e) => {
    const carro = carros.find(c => c.id === Number(e.target.value));
    setForm({
      ...form,
      carroId: e.target.value,
      carroMarca: carro?.marca || '',
      carroModelo: carro?.modelo || '',
    });
  };

  const statusBadge = (status) => {
    const classe = {
      AGENDADO: 'azul',
      REALIZADO: 'verde',
      CANCELADO: 'vermelho',
    };
    return <span className={`badge ${classe[status]}`}>{status}</span>;
  };

  return (
    <div>
      <h2>🚗 Test Drive</h2>

      {/* Estatísticas */}
      {estatisticas && (
        <div className="cards-grid">
          <div className="card-stat">
            <div className="card-stat-valor escuro">{estatisticas.total}</div>
            <div className="card-stat-label">Total</div>
          </div>
          <div className="card-stat">
            <div className="card-stat-valor azul">{estatisticas.agendados}</div>
            <div className="card-stat-label">Agendados</div>
          </div>
          <div className="card-stat">
            <div className="card-stat-valor verde">{estatisticas.realizados}</div>
            <div className="card-stat-label">Realizados</div>
          </div>
          <div className="card-stat">
            <div className="card-stat-valor vermelho">{estatisticas.cancelados}</div>
            <div className="card-stat-label">Cancelados</div>
          </div>
        </div>
      )}

      {/* Formulário */}
      <form onSubmit={agendar} className="form-card">
        <h3>Agendar Test Drive</h3>
        <div className="form-grid">
          <select value={form.carroId} onChange={selecionarCarro}>
            <option value="">Selecione o carro *</option>
            {carros.map(c => (
              <option key={c.id} value={c.id}>
                {c.marca} {c.modelo} ({c.ano}) — R$ {c.preco?.toLocaleString('pt-BR')}
              </option>
            ))}
          </select>
          <input
            placeholder="Nome do cliente *"
            value={form.clienteNome}
            onChange={e => setForm({ ...form, clienteNome: e.target.value })}
          />
          <input
            placeholder="Telefone do cliente *"
            value={form.clienteTelefone}
            onChange={e => setForm({ ...form, clienteTelefone: e.target.value })}
          />
          <input
            type="datetime-local"
            value={form.dataAgendada}
            onChange={e => setForm({ ...form, dataAgendada: e.target.value })}
          />
          <input
            placeholder="Observações (opcional)"
            value={form.observacoes}
            onChange={e => setForm({ ...form, observacoes: e.target.value })}
          />
        </div>
        {erro && <p className="erro">{erro}</p>}
        {sucesso && <p className="sucesso">{sucesso}</p>}
        <button type="submit">📅 Agendar Test Drive</button>
      </form>

      {/* Filtros */}
      <div className="form-card">
        <h3>Filtrar por Status</h3>
        <div className="filtros-row">
          {[
            { valor: '', label: '📋 Todos' },
            { valor: 'AGENDADO', label: '📅 Agendados' },
            { valor: 'REALIZADO', label: '✅ Realizados' },
            { valor: 'CANCELADO', label: '❌ Cancelados' },
          ].map(({ valor, label }) => (
            <button
              key={valor}
              className={`btn-selecao ${filtroStatus === valor ? 'ativo' : ''}`}
              onClick={() => filtrarPorStatus(valor)}>
              {label}
            </button>
          ))}
          <button className="btn-filtro cinza" onClick={carregar}>🔄 Atualizar</button>
        </div>
      </div>

      {carregando && <p style={{ color: '#888', margin: '10px 0' }}>Carregando...</p>}

      {/* Tabela */}
      <table className="tabela">
        <thead>
          <tr>
            <th>ID</th>
            <th>Carro</th>
            <th>Cliente</th>
            <th>Telefone</th>
            <th>Data Agendada</th>
            <th>Status</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          {testDrives.length === 0 && !carregando
            ? <tr><td colSpan="7" className="centro" style={{ padding: '20px', color: '#888' }}>
                Nenhum test drive encontrado.
              </td></tr>
            : testDrives.map(td => (
              <tr key={td.id}>
                <td>{td.id}</td>
                <td>{td.carroMarca} {td.carroModelo}</td>
                <td>{td.clienteNome}</td>
                <td>{td.clienteTelefone}</td>
                <td className="nowrap">
                  {new Date(td.dataAgendada).toLocaleString('pt-BR')}
                </td>
                <td>{statusBadge(td.status)}</td>
                <td>
                  <div className="filtros-row" style={{ gap: '6px' }}>
                    {td.status === 'AGENDADO' && (
                      <>
                        <button className="btn-filtro verde" onClick={() => realizar(td.id)}>
                          ✓ Realizar
                        </button>
                        <button className="btn-filtro cinza" onClick={() => cancelar(td.id)}>
                          ✕ Cancelar
                        </button>
                      </>
                    )}
                  </div>
                </td>
              </tr>
            ))
          }
        </tbody>
      </table>
    </div>
  );
}