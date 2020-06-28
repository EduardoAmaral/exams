import axios from 'axios';

axios.defaults.headers['Access-Control-Allow-Origin'] = '*';
axios.defaults.headers['Access-Control-Allow-Methods'] =
  'GET, POST, PATCH, PUT, DELETE, OPTIONS';
axios.defaults.headers['Access-Control-Allow-Credentials'] = true;
axios.defaults.headers['Access-Control-Allow-Headers'] =
  'Origin, X-Requested-With, Content-Type, Accept';
