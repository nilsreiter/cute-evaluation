SILVER_BASE_DIR=/Users/reiterns/Documents/CRETA/cute/silver

all:
	curl "http://localhost:8080/creta.ws.entitydetection/predict?project_id=5&document_id=12&model_id=31" > $(SILVER_BASE_DIR)/ims2+case+tt/parzival/3_12_26.xmi

	curl "http://localhost:8080/creta.ws.entitydetection/predict?project_id=5&document_id=12&model_id=34" > $(SILVER_BASE_DIR)/ims2-1.3.0/parzival/3_12_26.xmi
