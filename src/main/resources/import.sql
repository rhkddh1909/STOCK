INSERT INTO STOCK_INFO(STOCK_CODE,STOCK_NAME,STARTING_PRICE,CURRENT_PRICE,SELLING_COUNT,BUYING_COUNT,HITS,MARKET_CODE, MARKET_NAME, MARKET_NATION) SELECT stock_code,stock_name,price,price,0,0,0,'0001','KOSPI', 'KOR' FROM CSVREAD('classpath:KospiStocks.csv');
INSERT INTO STOCK_INFO(STOCK_CODE,STOCK_NAME,STARTING_PRICE,CURRENT_PRICE,SELLING_COUNT,BUYING_COUNT,HITS,MARKET_CODE, MARKET_NAME, MARKET_NATION) SELECT stock_code,stock_name,price,price,0,0,0,'0002','KOSDAQ', 'KOR' FROM CSVREAD('classpath:KosdaqStocks.csv');

INSERT INTO MARKET_INFO(MARKET_CODE,MARKET_NAME, MARKET_OPEN_YN, MARKET_NATION, MARKET_SEQUENCE) VALUES ('0001', 'KOSPI','N','KOR',0);
INSERT INTO MARKET_INFO(MARKET_CODE,MARKET_NAME, MARKET_OPEN_YN, MARKET_NATION, MARKET_SEQUENCE) VALUES ('0002', 'KOSDAQ','N','KOR',0);