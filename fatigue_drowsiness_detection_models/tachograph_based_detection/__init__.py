__author__ = 'mpages1'

from .lstm_fatigue_detection_model import (
    train_tachograph_model,
    predict_fatigue_tachograph,
    start_fatigue_monitoring,
    stop_fatigue_monitoring
)

__all__ = [
    'train_tachograph_model',
    'predict_fatigue_tachograph',
    'start_fatigue_monitoring',
    'stop_fatigue_monitoring',
]

from .main import start_tachograph_simulation
from lstm_fatigue_detection_model import stop_fatigue_monitoring
from generate_tachograph_data import start_tachograph_simulation, stop_tachograph_simulation
