import axios from 'axios';

const api = axios.create({ baseURL: 'http://localhost:8080/api' });

export const carroService = {
  listarTodos: () => api.get('/carros'),
  listarDisponiveis: () => api.get('/carros/disponiveis'),
  buscarPorId: (id) => api.get(`/carros/${id}`),
  cadastrar: (dados) => api.post('/carros', dados),
  atualizar: (id, dados) => api.put(`/carros/${id}`, dados),
  deletar: (id) => api.delete(`/carros/${id}`),

  listarPorMarca: (nome) => api.get(`/carros/marca?nome=${nome}`),
  listarPorPreco: (min, max) => api.get(`/carros/preco?min=${min}&max=${max}`),
  listarPorAno: (minAno) => api.get(`/carros/ano?minAno=${minAno}`),
  estatisticas: () => api.get('/carros/estatisticas'),
};

export const clienteService = {
  listarTodos: () => api.get('/clientes'),
  cadastrar: (dados) => api.post('/clientes', dados),
  atualizar: (id, dados) => api.put(`/clientes/${id}`, dados),
  deletar: (id) => api.delete(`/clientes/${id}`),
};

export const vendaService = {
  listarTodas: () => api.get('/vendas'),
  realizar: (dados) => api.post('/vendas', dados),

  listarPorValorMinimo: (valor) => api.get(`/vendas/valor?min=${valor}`),
  receitaTotal: () => api.get('/vendas/receita'),
  vendasMesAtual: () => api.get('/vendas/mes-atual'),
};

export const auditoriaService = {
  listarTodos: () => api.get('/auditoria'),
  listarPorEntidade: (entidade) => api.get(`/auditoria/entidade/${entidade}`),
  listarPorEntidadeEId: (entidade, id) => api.get(`/auditoria/entidade/${entidade}/${id}`),
};

export const testDriveService = {
  listarTodos: () => api.get('http://localhost:8081/api/testdrive'),
  listarPorStatus: (status) => api.get(`http://localhost:8081/api/testdrive/status?status=${status}`),
  buscarPorId: (id) => api.get(`http://localhost:8081/api/testdrive/${id}`),
  estatisticas: () => api.get('http://localhost:8081/api/testdrive/estatisticas'),
  agendar: (dados) => api.post('http://localhost:8081/api/testdrive', dados),
  realizar: (id) => api.patch(`http://localhost:8081/api/testdrive/${id}/realizar`),
  cancelar: (id) => api.patch(`http://localhost:8081/api/testdrive/${id}/cancelar`),
  deletar: (id) => api.delete(`http://localhost:8081/api/testdrive/${id}`),
};