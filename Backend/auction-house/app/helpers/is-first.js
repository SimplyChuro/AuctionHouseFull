import { helper } from '@ember/component/helper';

export function isFirst(params) {
  const index = params[0];

  if (index === 0) {
    return true;
  } else {
    return false;
  }
  
}

export default helper(isFirst);
