package source.connector;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.utils.AppInfoParser;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import source.task.HexGraphSourceTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static source.config.HexGraphSourceConnectorConfig.FOLDER_NAME;
import static source.config.HexGraphSourceConnectorConfig.TOPIC_NAME;

public class HexGraphSourceConnector extends SourceConnector {
    public static Logger LOG = LoggerFactory.getLogger(HexGraphSourceConnector.class);

    private String topic;
    private String folderName;

    @Override
    public String version() {
        return AppInfoParser.getVersion();
    }

    @Override
    public void start(Map<String, String> props) {
        LOG.info("Image source connector is starting.");

        topic = props.get(TOPIC_NAME);
        folderName = props.get(FOLDER_NAME);

        if (StringUtils.isBlank(topic)) {
            throw new ConnectException("Topic must not be empty.");
        }

        if (StringUtils.isBlank(folderName)) {
            throw new ConnectException("Folder name must not be empty.");
        }
    }

    @Override
    public Class<? extends Task> taskClass() {
        return HexGraphSourceTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        List<Map<String, String>> configs = new ArrayList<>();

        Map<String, String> config = new HashMap<>();
        config.put(TOPIC_NAME, topic);
        config.put(FOLDER_NAME, folderName);

        configs.add(config);
        return configs;
    }

    @Override
    public void stop() {
        LOG.info("Image source connector is stopping.");
    }

    @Override
    public ConfigDef config() {
        return new ConfigDef()
                .define(TOPIC_NAME, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Topic name")
                .define(FOLDER_NAME, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Folder name");
    }
}
