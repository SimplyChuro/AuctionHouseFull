import { helper } from '@ember/component/helper';

const isEqual = (params) => params[0] == params[1];

export default helper(isEqual);
