from app import create_app
import logging

logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s'
)

if __name__ == "__main__":
    app = create_app()
    logging.info("Flask app starting on port 5000")
    app.run(port=5000, debug=True)