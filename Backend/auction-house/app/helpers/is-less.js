import { helper } from '@ember/component/helper';

export function isLess(params) {
  const itemValue = params[0];
  const statementValue = params[1];

  if (statementValue > itemValue) {
    return true;
  } else {
    return false;
  }
}

export default helper(isLess);
