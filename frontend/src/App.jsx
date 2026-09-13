import { useState, useEffect } from 'react'
import './App.css'

function App() {
  const [cpf, setCpf] = useState('')
  const [senha, setSenha] = useState('')

  const [logado, setLogado] = useState(false)
  const [token, setToken] = useState('')
  const [idConta, setIdConta] = useState(null)
  
  const [saldo, setSaldo] = useState(0)
  const [extrato, setExtrato] = useState([])
  const [valorOperacao, setValorOperacao] = useState('')
  
  const [atualizarDados, setAtualizarDados] = useState(0)

  async function handleLogin(e) {
    e.preventDefault()
    try {
      const resposta = await fetch('http://localhost:8082/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ cpf, senha })
      });

      if (resposta.ok) {
        const dados = await resposta.json();
        const jwt = dados.token;
        const payload = JSON.parse(atob(jwt.split('.')[1]));
        
        setIdConta(payload.id);
        setToken(jwt);
        setLogado(true);
      } else {
        alert("Ops! CPF ou Senha incorretos.");
      }
    } catch (erro) {
      alert("Erro de conexão! O Backend Java está rodando?");
    }
  }

  async function realizarOperacao(tipoDaOperacao) {
    if (!valorOperacao || Number(valorOperacao) <= 0) {
      alert("Por favor, digite um valor válido maior que zero.");
      return;
    }

    try {
      // Endpoint corrigido: /contas/{id}/saque ou /contas/{id}/deposito
      const endpoint = `http://localhost:8082/contas/${idConta}/${tipoDaOperacao}`;
      
      const resposta = await fetch(endpoint, {
        method: 'POST',
        headers: { 
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}` 
        },
        body: JSON.stringify({ valor: Number(valorOperacao) })
      });

      if (resposta.ok) {
        setValorOperacao(''); 
        setAtualizarDados(atualizarDados + 1); // Recarrega Saldo e Extrato
      } else {
        const erroMsg = await resposta.text();
        alert(`Operação recusada: ${erroMsg}`);
      }
    } catch (erro) {
      alert("Erro ao processar a operação no servidor.");
    }
  }

  // Busca os dados da conta sempre que logar ou fizer uma operação
  useEffect(() => {
    if (logado) {
      // 1. Busca os dados da Conta (para pegar o Saldo atualizado)
      fetch(`http://localhost:8082/contas/${idConta}`, {
        headers: { 'Authorization': `Bearer ${token}` }
      })
      .then(res => res.json())
      .then(dados => setSaldo(dados.saldo))

      // 2. Busca o Extrato histórico
      fetch(`http://localhost:8082/contas/${idConta}/extrato`, {
        headers: { 'Authorization': `Bearer ${token}` }
      })
      .then(res => res.json())
      .then(dados => setExtrato(dados))
    }
  }, [logado, idConta, token, atualizarDados])

  function handleLogout() {
    setLogado(false);
    setToken('');
    setIdConta(null);
    setExtrato([]);
    setSaldo(0);
    setCpf('');
    setSenha('');
  }

  if (logado) {
    return (
      <div className="container">
        <div className="glass-panel">
          
          <header className="dashboard-header">
            <h1 className="logo-small">Secure<span>Bank</span></h1>
            <button onClick={handleLogout} className="btn-sair">Sair</button>
          </header>

          <div className="saldo-card">
            <p>Saldo Disponível</p>
            <h2>R$ {saldo.toFixed(2)}</h2>
          </div>
          
          <div className="operacoes-box">
            <h3>Movimentar Conta</h3>
            <input 
              type="number" 
              placeholder="R$ 0.00" 
              value={valorOperacao}
              onChange={(e) => setValorOperacao(e.target.value)}
              className="input-operacao"
            />
            <div className="botoes-acao">
              <button onClick={() => realizarOperacao('deposito')} className="btn-base btn-depositar">
                + Depositar
              </button>
              <button onClick={() => realizarOperacao('saque')} className="btn-base btn-sacar">
                - Sacar
              </button>
            </div>
          </div>

          <div className="extrato-section">
            <h3>Histórico Recente</h3>
            <div className="lista-extrato">
              {extrato.length === 0 ? (
                <p style={{ textAlign: 'center', color: '#78909c', marginTop: '10px' }}>
                  Sua conta ainda não possui transações.
                </p>
              ) : (
                [...extrato].reverse().map(transacao => (
                  <div key={transacao.id} className="transacao-card" style={{ borderLeft: `5px solid ${transacao.tipo === 'DEPOSITO' ? '#4caf50' : '#f44336'}` }}>
                    <div className="transacao-info">
                      <span className="transacao-titulo">{transacao.tipo}</span>
                      <span className="transacao-data">{new Date(transacao.dataHora).toLocaleString()}</span>
                    </div>
                    <span className={transacao.tipo === 'DEPOSITO' ? 'valor-positivo' : 'valor-negativo'}>
                      {transacao.tipo === 'DEPOSITO' ? '+' : '-'} R$ {transacao.valor.toFixed(2)}
                    </span>
                  </div>
                ))
              )}
            </div>
          </div>

        </div>
      </div>
    )
  }

  // Tela de Login
  return (
    <div className="container">
      <div className="glass-panel">
        <h1 className="logo">Secure<span>Bank</span></h1>
        <p className="subtitulo">Acesse sua conta com segurança</p>

        <form onSubmit={handleLogin}>
          <div className="campo">
            <label>CPF</label>
            <input 
              type="text" 
              placeholder="Digite seu CPF"
              value={cpf}
              onChange={(e) => setCpf(e.target.value)}
              required
            />
          </div>

          <div className="campo">
            <label>Senha</label>
            <input 
              type="password" 
              placeholder="Digite sua senha"
              value={senha}
              onChange={(e) => setSenha(e.target.value)}
              required
            />
          </div>

          <button type="submit" className="btn-base btn-entrar">
            Entrar na Conta
          </button>
        </form>
      </div>
    </div>
  )
}

export default App
