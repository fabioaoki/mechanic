import requests
import json
import os
import psycopg2
from psycopg2 import sql

# Cabeçalhos HTTP
headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36'
}

# Lista de modelos populares por marca
popular_models_by_brand = {
    "Toyota": ["Corolla", "Yaris", "Etios", "Hilux", "SW4", "RAV4", "Prius", "Camry", "Land Cruiser", "Avensis", "Fielder"],
    "Volkswagen": ["Gol", "Voyage", "Polo", "Virtus", "Tiguan", "T-Cross", "Nivus", "Jetta", "Amarok", "Passat"],
    "Fiat": ["Uno", "Palio", "Cronos", "Toro", "Mobi", "Strada", "Argo", "Siena", "Doblò", "Fiorino"],
    "Chevrolet": ["Onix", "Prisma", "Tracker", "Spin", "Cruze", "S10", "Montana", "Trailblazer", "Equinox", "Celta"],
    "Ford": ["Ka", "Fiesta", "EcoSport", "Ranger", "Fusion", "Focus", "Mustang", "Territory", "Edge", "Maverick"]
}

# Lista para armazenar os dados processados
processed_cars = []

# Iterar sobre todas as marcas e seus modelos populares
for brand_name, brand_models in popular_models_by_brand.items():
    for model_name in brand_models:
        # URL da API CarQuery para obter os detalhes do modelo específico
        url_trims = f"https://www.carqueryapi.com/api/0.3/?cmd=getTrims&make={brand_name.lower()}&model={model_name.lower()}"
        
        # Fazer a solicitação GET para obter os detalhes do modelo
        response_trims = requests.get(url_trims, headers=headers)
        
        # Verificar se a resposta foi bem-sucedida
        if response_trims.status_code == 200:
            try:
                trims_data = response_trims.json()
            except json.JSONDecodeError:
                print(f"Erro ao decodificar a resposta JSON para o modelo {model_name} da marca {brand_name}")
                trims_data = None
        else:
            print(f"Erro na solicitação para o modelo {model_name} da marca {brand_name}: {response_trims.status_code}")
            trims_data = None

        if trims_data:
            # Processar os dados para obter as informações relevantes
            for trim in trims_data['Trims']:
                year = trim.get('model_year', 'Desconhecido')
                if year != 'Desconhecido':
                    year = str(year)  # Convertendo ano para string

                car_info = {
                    'model_make_id': trim['model_make_id'],
                    'model_name': trim['model_name'],
                    'model_trim': trim.get('model_trim', 'Desconhecido'),
                    'model_year': year,
                    'model_transmission_type': trim.get('model_transmission_type', 'Desconhecido')
                }
                processed_cars.append(car_info)

# Caminho absoluto para salvar o arquivo
file_path = os.path.join('C:\\Users\\fabio\\OneDrive\\Área de Trabalho', 'popular_cars.json')
with open(file_path, 'w') as f:
    json.dump(processed_cars, f, indent=4)

print(f"Dados processados e salvos em '{file_path}'")

# Conectar ao banco de dados PostgreSQL e inserir os dados na tabela marc
try:
    connection = psycopg2.connect(
        user="postgres",
        password="082112",
        host="localhost",
        port="5432",
        database="postgres"
    )
    cursor = connection.cursor()

    # Função para verificar se o registro já existe
    def record_exists(car):
        check_query = sql.SQL("""
            SELECT 1 FROM mechanic.model WHERE
            name = %s AND model = %s AND version = %s AND year::VARCHAR = %s AND transmission_type = %s
        """)
        cursor.execute(check_query, (
            car['model_make_id'],
            car['model_name'],
            car['model_trim'],
            car['model_year'],
            car['model_transmission_type']
        ))
        return cursor.fetchone() is not None
    
    # Inserir dados na tabela marc
    insert_query = sql.SQL("""
        INSERT INTO mechanic.model (name, model, version, year, transmission_type)
        VALUES (%s, %s, %s, %s, %s)
    """)
    
    for car in processed_cars:
        if not record_exists(car):
            cursor.execute(insert_query, (
                car['model_make_id'],
                car['model_name'],
                car['model_trim'],
                car['model_year'],
                car['model_transmission_type']
            ))

    connection.commit()
    print("Dados inseridos na tabela 'marc' com sucesso")

except (Exception, psycopg2.Error) as error:
    print(f"Erro ao conectar ao PostgreSQL: {error}")
finally:
    if connection:
        cursor.close()
        connection.close()
        print("Conexão com o PostgreSQL encerrada")


# pip install requests
# pip install psycopg2-binary
# python fetch_popular_cars_and_save_to_db.py

