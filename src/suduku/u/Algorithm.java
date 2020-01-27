package suduku.u;

public class Algorithm {

    public static int[][] calc(int[][] input) {
        /*将输入的二维数独数据转换成合适的形式. 下面的数据包括头, 不包括尾
        0 ~ 81      一维的数独数据. 已知数转换成相应的位, 比如2 映射成 0 0000 0010,未知数映射成 1 1111 1111
        81 ~ 90     指定 行 的所有 确定性数据 的 or 的结果
        90 ~ 99     指定 列 的所有 确定性数据 的 or 的结果
        99 ~ 108    指定 块 的所有 确定性数据 的 or 的结果
        108 ~ 117   指定 行 的所有 确定性数据 的 xor 的结果
        117 ~ 126   指定 列 的所有 确定性数据 的 xor 的结果
        129 ~ 135   指定 块 的所有 确定性数据 的 xor 的结果*/
        int[] data = new int[135];

        //初始化 data 的第 1 段
        for (int i = 0; i < 81; i++) {
            data[i] = input[i % 9][i / 9] > 0 ? 0x1 << (input[i % 9][i / 9] - 1) : 0b111_111_111;
        }

        //初始化 data 的后 6 段
        if (!initAndCheck(data)) {
            return null;
        }

        int[] result = tryFill(data);
        if (result == null) {
            return null;
        }

        for (int i = 0; i < 81; i++) {
            int num = result[i];
            input[i % 9][i / 9] = isOneBit(num) ? oneBitToInt(num) : -1;
        }

        return input;
    }

    //检查该数字的二进制是否只包含一个1
    private static boolean isOneBit(int v) {
        return (v & -v) == v;
    }

    //求得只含一位二进制1的数的1的位置
    private static int oneBitToInt(int bit) {
        for (int k = 0; k < 9; k++)
            if (((bit >> k) & 0x1) == 1)
                return k + 1;

        return -1;
    }

    //首次运行执行冲突检查,并初始化 data 的后 6 段
    private static boolean initAndCheck(int[] data) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int v = data[i * 9 + j];
                if (isOneBit(v) && isFillFailed(data, v, i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    //检查填充是否失败. 只含一位二进制1的几个数, 如果各不相等, 那么 or 与 xor 的结果应该相同
    private static boolean isFillFailed(int[] data, int v, int i, int j) {
        int blockPos = i / 3 + j / 3 * 3;
        int a = data[81 + i] |= v;
        int b = data[108 + i] ^= v;
        int c = data[90 + j] |= v;
        int d = data[117 + j] ^= v;
        int e = data[99 + blockPos] |= v;
        int f = data[126 + blockPos] ^= v;
        return a != b | c != d | e != f;
    }

    private static int[] tryFill(int[] data) {
        boolean findUnknownNum;
        int x = 0, y = 0;

        //尽可能的填入数字,并对填入的每一个数字进行冲突检测
        boolean fillNumSuccess;
        do {
            fillNumSuccess = false;
            findUnknownNum = false;
            int oldBitCount = 10;

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    int pos = i * 9 + j;
                    //只有未知的位置才进行计算
                    if (!isOneBit(data[pos])) {
                        int v = data[pos] &= ~(data[81 + i] | data[90 + j] | data[99 + i / 3 + j / 3 * 3]);//消去当前位置的可能性
                        if (v == 0) {//把未知数的可能性给消没了, 肯定是出问题了
                            return null;
                        } else if (!isOneBit(v)) {//如果这个位置依然是未知的, 记录
                            findUnknownNum = true;
                            int newBitCount = Integer.bitCount(v);
                            if (newBitCount < oldBitCount) {//挑一个可能性少的位置应该可以减少猜测的次数
                                oldBitCount = newBitCount;
                                x = i;
                                y = j;
                            }
                        } else if (isFillFailed(data, v, i, j)) {//只剩下一种可能, 测试填入的数是否冲突
                            return null;
                        } else {//填充成功
                            fillNumSuccess = true;
                        }
                    }
                }
            }
        } while (fillNumSuccess);

        //已经无法再进行确定性填入了, 选一个未知数, 猜测其值, 进行尝试
        if (findUnknownNum) {
            int pos = x * 9 + y;
            int multi = data[pos];
            int guess;

            while ((guess = multi & -multi) > 0) {//对未知数的每一种可能进行尝试
                multi -= guess;

                int[] dataCopy = data.clone();
                dataCopy[pos] = guess;
                isFillFailed(dataCopy, guess, x, y);//只为了更新相关的数据段, 一定不会失败

                //递归
                int[] result = tryFill(dataCopy);
                if (result != null) {
                    return result;
                }
            }

            //所有尝试均告失败
            return null;
        } else {
            //计算成功
            return data;
        }
    }
}
