const isProduct = process.env.NODE_ENV === 'production' ? true : false;
const PORT = 3090;

const SECRET = 'irony';

module.exports = {
    isProduct,
    PORT,
    SECRET
}