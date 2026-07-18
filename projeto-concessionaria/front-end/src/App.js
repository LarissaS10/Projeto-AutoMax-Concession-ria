import React, { useState } from 'react';
import Carros from './pages/Carros';
import Clientes from './pages/Clientes';
import Vendas from './pages/Vendas';
import Auditoria from './pages/Auditoria';
import './App.css';

function App() {
  const [pagina, setPagina] = useState('carros');

  return (
    <div className="app">
      <header>
        <h1>Concessionária AutoMax</h1>
        <nav>
          <button onClick={() => setPagina('carros')}
            className={pagina === 'carros' ? 'ativo' : ''}>
            🚘 Carros
          </button>
          <button onClick={() => setPagina('clientes')}
            className={pagina === 'clientes' ? 'ativo' : ''}>
            👤 Clientes
          </button>
          <button onClick={() => setPagina('vendas')}
            className={pagina === 'vendas' ? 'ativo' : ''}>
            💰 Vendas
          </button>
          <button onClick={() => setPagina('auditoria')}
            className={pagina === 'auditoria' ? 'ativo' : ''}>
            📋 Auditoria
          </button>
        </nav>
      </header>

      <main>
        {pagina === 'carros' && <Carros />}
        {pagina === 'clientes' && <Clientes />}
        {pagina === 'vendas' && <Vendas />}
        {pagina === 'auditoria' && <Auditoria />}
      </main>
    </div>
  );
}

export default App;