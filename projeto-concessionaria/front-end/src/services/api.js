import axios from 'axios';

const api = axios.create({ baseURL: 'http://localhost:8080/api' });

export const carroService = {
  listarTodos: () => api.get('/carros'),
  listarDisponiveis: () => api.get('/carros/disponiveis'),
  buscarPorId: (id) => api.get(`/carros/${id}`),
  cadastrar: (dados) => api.post('/carros', dados),
  atualizar: (id, dados) => api.put(`/carros/${id}`, dados),
  deletar: (id) => api.delete(`/carros/${id}`),
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
};