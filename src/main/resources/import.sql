INSERT INTO stock (symbol, name, exchange)
VALUES ('AAPL', 'Apple Inc', 'NASDAQ')
ON CONFLICT (symbol) DO NOTHING;

INSERT INTO stock (symbol, name, exchange)
VALUES ('MSFT', 'Microsoft Corporation', 'NASDAQ')
ON CONFLICT (symbol) DO NOTHING;