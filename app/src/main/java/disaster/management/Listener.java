package disaster.management;

public interface Listener {
    void OnDownloadResult(int ResponseCode, Object Response);

    void OnErrorDownloadResult(int ResponseCode);
}
